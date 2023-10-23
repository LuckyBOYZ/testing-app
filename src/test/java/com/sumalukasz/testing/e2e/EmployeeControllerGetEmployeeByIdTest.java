package com.sumalukasz.testing.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumalukasz.config.ClearDatabase;
import com.sumalukasz.config.IntegrationTest;
import com.sumalukasz.config.MigrateDatabase;
import com.sumalukasz.config.TestBeansConfiguration;
import com.sumalukasz.testing.model.dto.EmployeeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "UnnecessaryUnicodeEscape"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@DisplayName("Testing 'getEmployeeById' Endpoint")
@Tag("Controller")
@Import(TestBeansConfiguration.class)
class EmployeeControllerGetEmployeeByIdTest extends IntegrationTest {

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
    void shouldReturnStatus200AndEmployeeWithId3() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port, 3)))
                .build();

        //when
        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        String body = res.body();
        EmployeeDto employeeDto = objectMapper.readValue(body, EmployeeDto.class);

        //then
        assertEquals(HttpStatus.OK.value(), res.statusCode());
        assertEquals("adcdc4f2-896a-4fcc-b2cc-0903f1acdc03", employeeDto.getUuid());
        assertEquals("Tadeusz", employeeDto.getName());
        assertEquals("Komornik\u00f3w", employeeDto.getSurname());
        assertEquals("76101720170", employeeDto.getPesel());
        assertEquals("66 761 71 46", employeeDto.getPhoneNumber());
        assertEquals(LocalDate.parse("1976-10-17"), employeeDto.getDateOfBirth());
        assertEquals(3, employeeDto.getDepartmentId());
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus400AndErrorMessageWhenIdIsLessThan1() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port, 0)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> map = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("'id' path variable cannot be less than 1", map.get("errorMessage"));
        assertEquals("0", map.get("value"));
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus400AndErrorMessageWhenPageIsNotANumber() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port, "1a")))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> map = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("'id' parameter must be the number", map.get("errorMessage"));
        assertEquals("1a", map.get("value"));
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus404AndErrorMessageWhenThereIsNoDataForGivenParameters() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port, 156)))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }
}
