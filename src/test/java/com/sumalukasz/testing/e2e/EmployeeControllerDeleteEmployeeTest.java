package com.sumalukasz.testing.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumalukasz.config.ClearDatabase;
import com.sumalukasz.config.IntegrationTest;
import com.sumalukasz.config.MigrateDatabase;
import com.sumalukasz.config.TestBeansConfiguration;
import com.sumalukasz.testing.model.entity.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("ALL")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@DisplayName("Testing 'deleteEmployee' Endpoint")
@Tag("Controller")
@Import(TestBeansConfiguration.class)
class EmployeeControllerDeleteEmployeeTest extends IntegrationTest {

    private static final String BASIC_URL = "http://localhost:%s/employees/%s";
    private static final String GET_UUID_FROM_EMPLOYEE_ID_MAPPING_BY_EMPLOYEE_ID = "SELECT UUID FROM EMPLOYEE_ID_MAPPING WHERE EMPLOYEE_ID = :employeeId";
    private static final String GET_ADDRESS_FROM_ADDRESS_BY_EMPLOYEE_ID = "SELECT STREET, HOUSE_NUMBER, PREMISES_NUMBER, POSTCODE, CITY FROM ADDRESS WHERE EMPLOYEE_ID = :employeeId";

    @LocalServerPort
    private int port;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldDeleteEmployeeWithId10() throws IOException, InterruptedException {
        //given
        HttpRequest deleteEmployeeById = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL.formatted(port, 10)))
                .build();
        HttpRequest getEmployeeById = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port, 10)))
                .build();

        //when
        String uuidBeforeDeleting = getUuidFromEmployeeIdMappingByEmployeeId(10);
        Address addressBeforeDeleting = getAddressFromAddressByEmployeeId(10);
        HttpResponse<String> beforeDeleting = httpClient.send(getEmployeeById, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> employeeProperties = objectMapper.readValue(beforeDeleting.body(), new TypeReference<>() {
        });
        HttpResponse<String> deleteResponse = httpClient.send(deleteEmployeeById, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> afterDeleting = httpClient.send(getEmployeeById, HttpResponse.BodyHandlers.ofString());
        String uuidAfterDeleting = getUuidFromEmployeeIdMappingByEmployeeId(10);
        Address addressAfterDeleting = getAddressFromAddressByEmployeeId(10);

        //then
        assertEquals(uuidBeforeDeleting, employeeProperties.get("uuid"));
        assertEquals("ul. Akademii Umiej\u0119tno\u015bci", addressBeforeDeleting.street());
        assertEquals("5", addressBeforeDeleting.houseNumber());
        assertEquals("4", addressBeforeDeleting.premisesNumber());
        assertEquals("40-004", addressBeforeDeleting.postcode());
        assertEquals("Katowice", addressBeforeDeleting.city());
        assertEquals("Barbara", employeeProperties.get("name"));
        assertEquals("Gruda", employeeProperties.get("surname"));
        assertEquals("16221267588", employeeProperties.get("pesel"));
        assertNull(employeeProperties.get("phoneNumber"));
        assertEquals(LocalDate.of(2016, 2, 12).toString(), employeeProperties.get("dateOfBirth"));
        assertEquals(6, employeeProperties.get("departmentId"));
        assertEquals(HttpStatus.NO_CONTENT.value(), deleteResponse.statusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), afterDeleting.statusCode());
        assertNull(uuidAfterDeleting);
        assertNull(addressAfterDeleting);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus204AndErrorMessageWhenEmployeeIdDoesntExistInDatabase() throws IOException, InterruptedException {
        //given
        HttpRequest deleteEmployee = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL.formatted(port, 1000)))
                .build();
        HttpRequest getEmployeeById = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port, 1000)))
                .build();

        //when
        String uuid = getUuidFromEmployeeIdMappingByEmployeeId(1000);
        Address address = getAddressFromAddressByEmployeeId(1000);
        HttpResponse<String> getResponse = httpClient.send(getEmployeeById, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> deleteResponse = httpClient.send(deleteEmployee, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), getResponse.statusCode());
        assertEquals(HttpStatus.NO_CONTENT.value(), deleteResponse.statusCode());
        assertNull(uuid);
        assertNull(address);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus400AndErrorMessageWhenIdIsNotANumber() throws IOException, InterruptedException {
        //given
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL.formatted(port, "abc")))
                .build();

        //when
        HttpResponse<String> deleteResponse = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> map = objectMapper.readValue(deleteResponse.body(), new TypeReference<>() {
        });

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), deleteResponse.statusCode());
        assertEquals("'id' parameter must be the number", map.get("errorMessage"));
        assertEquals("abc", map.get("value"));
    }

    private String getUuidFromEmployeeIdMappingByEmployeeId(long employeeId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("employeeId", employeeId);
        try {
            return jdbcTemplate.queryForObject(GET_UUID_FROM_EMPLOYEE_ID_MAPPING_BY_EMPLOYEE_ID, params, String.class);
        } catch (EmptyResultDataAccessException ignore) {
            return null;
        }
    }

    private Address getAddressFromAddressByEmployeeId(long employeeId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("employeeId", employeeId);
        try {
            return jdbcTemplate.queryForObject(GET_ADDRESS_FROM_ADDRESS_BY_EMPLOYEE_ID, params, new DataClassRowMapper<>(Address.class));
        } catch (EmptyResultDataAccessException ignore) {
            return null;
        }
    }

}
