package com.sumalukasz.testing.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumalukasz.config.ClearDatabase;
import com.sumalukasz.config.IntegrationTest;
import com.sumalukasz.config.MigrateDatabase;
import com.sumalukasz.config.TestBeansConfiguration;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@DisplayName("Testing 'deleteEmployee' Endpoint")
@Tag("Controller")
@Import(TestBeansConfiguration.class)
class EmployeeControllerDeleteEmployeeTest extends IntegrationTest {

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
    void shouldDeleteEmployeeWithId10() throws IOException, InterruptedException {
        //given
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL.formatted(port, 10)))
                .build();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port, 10)))
                .build();

        //when
        HttpResponse<String> deleteResponse = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), deleteResponse.statusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), getResponse.statusCode());
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus204AndErrorMessageWhenEmployeeIdDoesntExistInDatabase() throws IOException, InterruptedException {
        //given
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASIC_URL.formatted(port, 1000)))
                .build();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port, 1000)))
                .build();

        //when
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> deleteResponse = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), getResponse.statusCode());
        assertEquals(HttpStatus.NO_CONTENT.value(), deleteResponse.statusCode());
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus400AndErrorMessageWhenIdIsntANumber() throws IOException, InterruptedException {
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

}
