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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@DisplayName("Testing 'updateEmployee' Endpoint")
@Tag("Controller")
@Import(TestBeansConfiguration.class)
class EmployeeControllerUpdateEmployeeTest extends IntegrationTest {

    private static final String BASIC_URL = "http://localhost:%s/employees/%s";

    @LocalServerPort
    private int port;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldUpdateEmployeeWithFullFillFieldsForEmployeeIdEqualTo6() throws IOException, InterruptedException {
        //given
        EmployeeRequest employeeRequest = new EmployeeRequest("MyName", "MySurname", "987654321",
                "777 777 666", LocalDate.of(1950, 8, 10), 1L);
        String json = objectMapper.writeValueAsString(employeeRequest);
        HttpRequest patchRequest = HttpRequest.newBuilder()
                .method(HttpMethod.PATCH.name(), HttpRequest.BodyPublishers.ofString(json))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port, 6)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(patchRequest, HttpResponse.BodyHandlers.ofString());
        Employee expectedEmployee = objectMapper.readValue(response.body(), Employee.class);

        //then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals(employeeRequest.getName(), expectedEmployee.name());
        assertEquals(employeeRequest.getSurname(), expectedEmployee.surname());
        assertEquals(employeeRequest.getPesel(), expectedEmployee.pesel());
        assertEquals(employeeRequest.getPhoneNumber(), expectedEmployee.phoneNumber());
        assertEquals(employeeRequest.getDateOfBirth(), expectedEmployee.dateOfBirth());
        assertEquals(employeeRequest.getDepartmentId(), expectedEmployee.departmentId());
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldUpdateEmployeeNameAndSurnameAndPeselFieldsForEmployeeIdEqualTo10() throws IOException, InterruptedException {
        //given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Tom");
        requestBody.put("surname", "Cruise");
        requestBody.put("pesel", "88776611220");
        String json = objectMapper.writeValueAsString(requestBody);
        HttpRequest patchRequest = HttpRequest.newBuilder()
                .method(HttpMethod.PATCH.name(), HttpRequest.BodyPublishers.ofString(json))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port, 10)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(patchRequest, HttpResponse.BodyHandlers.ofString());
        Employee expectedEmployee = objectMapper.readValue(response.body(), Employee.class);

        //then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals(requestBody.get("name"), expectedEmployee.name());
        assertEquals(requestBody.get("surname"), expectedEmployee.surname());
        assertEquals(requestBody.get("pesel"), expectedEmployee.pesel());
        assertNull(expectedEmployee.phoneNumber());
        assertEquals(LocalDate.of(2016, 2, 12), expectedEmployee.dateOfBirth());
        assertEquals(6, expectedEmployee.departmentId());
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn400WhenAdditionalPropertiesAreAddedToRequestBody() throws IOException, InterruptedException {
        //given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Tom");
        requestBody.put("surname", "Cruise");
        requestBody.put("pesel", "88776611220");
        requestBody.put("something", "something");
        requestBody.put("errorField", "error");
        String json = objectMapper.writeValueAsString(requestBody);
        HttpRequest patchRequest = HttpRequest.newBuilder()
                .method(HttpMethod.PATCH.name(), HttpRequest.BodyPublishers.ofString(json))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uri(URI.create(BASIC_URL.formatted(port, 10)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(patchRequest, HttpResponse.BodyHandlers.ofString());
        String errorBody = response.body();
        Map<String, Object> parsed = objectMapper.readValue(errorBody, new TypeReference<>() {
        });
        String errorMessage = (String) parsed.get("errorMessage");
        Map<String, String> invalidFields = objectMapper.readValue((String) parsed.get("value"), new TypeReference<>() {
        });
        String somethingError = invalidFields.get("something");
        String errorFieldError = invalidFields.get("errorField");

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals(2, invalidFields.size());
        assertEquals("Request body has invalid fields", errorMessage);
        assertEquals("Unknown property name 'something'", somethingError);
        assertEquals("Unknown property name 'errorField'", errorFieldError);
    }

}
