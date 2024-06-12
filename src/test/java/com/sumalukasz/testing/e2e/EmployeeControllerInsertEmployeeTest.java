package com.sumalukasz.testing.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumalukasz.config.ClearDatabase;
import com.sumalukasz.config.IntegrationTest;
import com.sumalukasz.config.MigrateDatabase;
import com.sumalukasz.config.TestBeansConfiguration;
import com.sumalukasz.testing.model.entity.Employee;
import com.sumalukasz.testing.model.request.EmployeeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("ALL")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@DisplayName("Testing 'insertEmployee' Endpoint")
@Tag("Controller")
@Import(TestBeansConfiguration.class)
class EmployeeControllerInsertEmployeeTest extends IntegrationTest {

    private static final String GET_EMPLOYEE_ID_FROM_EMPLOYEE_ID_MAPPING_BY_UUID = "SELECT EMPLOYEE_ID FROM EMPLOYEE_ID_MAPPING WHERE UUID = :uuid";
    private static final String BASIC_URL = "http://localhost:%s/employees/";

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
    void shouldCreateEmployeeWithFulfillColumnsButWithoutDepartmentId() throws IOException, InterruptedException {
        //given
        EmployeeRequest employeeRequest = new EmployeeRequest("John", "Week", "12345678910",
                "123 456 789", LocalDate.of(2000, 5, 20), null);
        String json = objectMapper.writeValueAsString(employeeRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Optional<String> linkToNewValueOptional = response.headers().firstValue(HttpHeaders.LOCATION);
        String linkToNewValue = linkToNewValueOptional.get();
        HttpRequest secRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(linkToNewValue))
                .build();
        HttpResponse<String> newEmployee = httpClient.send(secRequest, HttpResponse.BodyHandlers.ofString());
        Employee employee = objectMapper.readValue(newEmployee.body(), Employee.class);
        long employeeId = getEmployeeIdFromEmployeeIdMappingByUuid(employee.uuid());

        //then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals("", response.body());
        String[] split = linkToNewValue.split("/");
        assertEquals(split[split.length - 1], employee.uuid());
        assertEquals("John", employee.name());
        assertEquals("Week", employee.surname());
        assertEquals("12345678910", employee.pesel());
        assertEquals("123 456 789", employee.phoneNumber());
        assertEquals("2000-05-20", employee.dateOfBirth().toString());
        assertNull(employee.departmentId());
        assertEquals(156, employeeId);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldCreateEmployeeWithFulfillColumns() throws IOException, InterruptedException {
        //given
        EmployeeRequest employeeRequest = new EmployeeRequest("John", "Week", "12345678910",
                "123 456 789", LocalDate.of(2000, 5, 20), 2L);
        String json = objectMapper.writeValueAsString(employeeRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Optional<String> linkToNewValueOptional = response.headers().firstValue(HttpHeaders.LOCATION);
        String linkToNewValue = linkToNewValueOptional.get();
        HttpRequest secRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(linkToNewValue))
                .build();
        HttpResponse<String> newEmployee = httpClient.send(secRequest, HttpResponse.BodyHandlers.ofString());
        Employee employee = objectMapper.readValue(newEmployee.body(), Employee.class);
        long employeeId = getEmployeeIdFromEmployeeIdMappingByUuid(employee.uuid());

        //then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals("", response.body());
        String[] split = linkToNewValue.split("/");
        assertEquals(split[split.length - 1], employee.uuid());
        assertEquals("John", employee.name());
        assertEquals("Week", employee.surname());
        assertEquals("12345678910", employee.pesel());
        assertEquals("123 456 789", employee.phoneNumber());
        assertEquals("2000-05-20", employee.dateOfBirth().toString());
        assertEquals(2L, employee.departmentId());
        assertEquals(156, employeeId);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn400WhenNameAndSurnameAndPeselIsNull() throws IOException, InterruptedException {
        //given
        EmployeeRequest employeeRequest = new EmployeeRequest(null, null, null,
                "123 456 789", LocalDate.of(2000, 5, 20), 2L);
        String json = objectMapper.writeValueAsString(employeeRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String errorBody = response.body();
        Map<String, Object> parsed = objectMapper.readValue(errorBody, new TypeReference<>() {
        });
        String errorMessage = (String) parsed.get("errorMessage");
        Map<String, String> invalidFields = objectMapper.readValue((String) parsed.get("value"), new TypeReference<>() {
        });

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals(3, invalidFields.size());
        assertEquals("Request body has invalid fields", errorMessage);
        assertEquals("'name' cannot be null", invalidFields.get("name"));
        assertEquals("'surname' cannot be null", invalidFields.get("surname"));
        assertEquals("'pesel' cannot be null", invalidFields.get("pesel"));
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn400WhenBodyIsEmpty() throws IOException, InterruptedException {
        //given
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String errorBody = response.body();
        Map<String, Object> parsed = objectMapper.readValue(errorBody, new TypeReference<>() {
        });
        String errorMessage = (String) parsed.get("errorMessage");

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("The body in the request is required", errorMessage);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn400WhenDepartmentDoesntExistForGivenDepartmenId() throws IOException, InterruptedException {
        //given
        EmployeeRequest employeeRequest = new EmployeeRequest("John", "Week", "12345678910",
                "123 456 789", LocalDate.of(2000, 5, 20), 100L);
        String json = objectMapper.writeValueAsString(employeeRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String errorBody = response.body();
        Map<String, Object> parsed = objectMapper.readValue(errorBody, new TypeReference<>() {
        });
        String errorMessage = (String) parsed.get("errorMessage");
        String departmentId = (String) parsed.get("value");

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("No department for given department id", errorMessage);
        assertEquals("100", departmentId);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn400WhenNameOrSurnameOrPeselOrPhoneNumberOrDepartmentDoesntFitIntoTheirMaximumLegth() throws IOException, InterruptedException {
        //given
        EmployeeRequest employeeRequest = new EmployeeRequest("ABCDEFGHIJKLMNOPRSTUVWXYZ", "ABCDEFGHIJKLMNOPRSTUVWXYZABCDEFGHIJKLMNOPRSTUVWXYZ",
                "12345678910111213",
                "12345678910111213", LocalDate.of(2000, 5, 20), 123456789123456789L);
        String json = objectMapper.writeValueAsString(employeeRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String errorBody = response.body();
        Map<String, Object> parsed = objectMapper.readValue(errorBody, new TypeReference<>() {
        });
        String errorMessage = (String) parsed.get("errorMessage");
        Map<String, Object> invalidFields = objectMapper.readValue((String) parsed.get("value"), new TypeReference<>() {
        });
        Object nameError = (String) invalidFields.get("name");
        String surnameError = (String) invalidFields.get("surname");
        String peselError = (String) invalidFields.get("pesel");
        String phoneNumberError = (String) invalidFields.get("phoneNumber");
        String dateOfBirthError = (String) invalidFields.get("departmentId");

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals(5, invalidFields.size());
        assertEquals("Request body has invalid fields", errorMessage);
        assertEquals("'name' has invalid length. Maximal length for this property is 16", nameError);
        assertEquals("'surname' has invalid length. Maximal length for this property is 45", surnameError);
        assertEquals("'pesel' has invalid length. Maximal length for this property is 11", peselError);
        assertEquals("'phoneNumber' has invalid length. Maximal length for this property is 15", phoneNumberError);
        assertEquals("'departmentId' has invalid length. Maximal length for this property is 11", dateOfBirthError);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn201WhenAdditionalPropertiesWillBeAddedAndRequiredPropetiesWillBeSetProperly() throws IOException, InterruptedException {
        //given
        Map<String, String> requestBody = Map.of("name", "ABCDEFGHI", "surname", "ABCDEGHI",
                "something", "12345678910111213", "abcd", "12345678910111213", "pesel", "123456789");
        String json = objectMapper.writeValueAsString(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Optional<String> linkToNewValueOptional = response.headers().firstValue(HttpHeaders.LOCATION);
        String linkToNewValue = linkToNewValueOptional.get();
        HttpRequest secRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(linkToNewValue))
                .build();
        HttpResponse<String> newEmployee = httpClient.send(secRequest, HttpResponse.BodyHandlers.ofString());
        Employee employee = objectMapper.readValue(newEmployee.body(), Employee.class);
        long employeeId = getEmployeeIdFromEmployeeIdMappingByUuid(employee.uuid());

        //then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals("", response.body());
        String[] split = linkToNewValue.split("/");
        assertEquals(split[split.length - 1], employee.uuid());
        assertEquals("ABCDEFGHI", employee.name());
        assertEquals("ABCDEGHI", employee.surname());
        assertEquals("123456789", employee.pesel());
        assertNull(employee.phoneNumber());
        assertNull(employee.dateOfBirth());
        assertNull(employee.departmentId());
        assertEquals(156, employeeId);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn415WhenContentTypeIsNotApplicationJson() throws IOException, InterruptedException {
        //given
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("abcdefgh"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String errorBody = response.body();
        Map<String, Object> parsed = objectMapper.readValue(errorBody, new TypeReference<>() {
        });
        String errorMessage = (String) parsed.get("errorMessage");

        //then
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), response.statusCode());
        assertEquals("Content-Type 'text/plain' is not supported", errorMessage);
    }

    private long getEmployeeIdFromEmployeeIdMappingByUuid(String uuid) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("uuid", uuid);
        try {
            return jdbcTemplate.queryForObject(GET_EMPLOYEE_ID_FROM_EMPLOYEE_ID_MAPPING_BY_UUID, params, Long.class);
        } catch (EmptyResultDataAccessException ignore) {
            return 0;
        }
    }

}
