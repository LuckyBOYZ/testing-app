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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@DisplayName("Testing 'getAllEmployees' Endpoint")
@Tag("Controller")
@Import(TestBeansConfiguration.class)
class EmployeeControllerGetAllEmployeesEndpointTest extends IntegrationTest {

    private static final String BASIC_URL = "http://localhost:%s/employees";

    @LocalServerPort
    private int port;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn10RowsWhenEndpointDoesntHaveQueryParameters() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port)))
                .build();

        //when
        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        String body = res.body();
        List<EmployeeDto> result = objectMapper.readValue(body, new TypeReference<>() {
        });

        //then
        assertEquals(10, result.size());
        Long[] array = result.stream()
                .map(EmployeeDto::getId)
                .toArray(Long[]::new);
        assertArrayEquals(new Long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L}, array);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn30RowsWhenOffsetIs30() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port).concat("?offset=30")))
                .build();

        //when
        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        String body = res.body();
        List<EmployeeDto> result = objectMapper.readValue(body, new TypeReference<>() {
        });

        //then
        assertEquals(30, result.size());
        Long[] array = result.stream()
                .map(EmployeeDto::getId)
                .toArray(Long[]::new);
        Long[] expected = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L,
                21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L, 30L};
        assertArrayEquals(expected, array);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn20RowsFrom3PageWhenOffsetIs20AndPageIs3() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port).concat("?offset=20&page=3")))
                .build();

        //when
        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        String body = res.body();
        List<EmployeeDto> result = objectMapper.readValue(body, new TypeReference<>() {
        });

        //then
        assertEquals(20, result.size());
        Long[] array = result.stream()
                .map(EmployeeDto::getId)
                .toArray(Long[]::new);
        Long[] expected = {41L, 42L, 43L, 44L, 45L, 46L, 47L, 48L, 49L, 50L, 51L,
                52L, 53L, 54L, 55L, 56L, 57L, 58L, 59L, 60L};
        assertArrayEquals(expected, array);
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus400AndErrorMessageWhenOffsetIsLessThan1() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port).concat("?offset=0")))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> map = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("'offset' parameter cannot be less than 1", map.get("errorMessage"));
        assertEquals("0", map.get("value"));
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus400AndErrorMessageWhenPageIsLessThan1() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port).concat("?page=0")))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> map = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("'page' parameter cannot be less than 1", map.get("errorMessage"));
        assertEquals("0", map.get("value"));
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus400AndErrorMessageWhenOffsetHasHigherValueThanIntegerMaxValue() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port).concat("?offset=123456789123")))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> map = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("'offset' parameter has too high value", map.get("errorMessage"));
        assertEquals("123456789123", map.get("value"));
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus400AndErrorMessageWhenPageHasHigherValueThanIntegerMaxValue() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port).concat("?page=123456789123")))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> map = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("'page' parameter has too high value", map.get("errorMessage"));
        assertEquals("123456789123", map.get("value"));
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus400AndErrorMessageWhenOffsetIsNotANumber() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port).concat("?offset=2b")))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> map = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("'offset' parameter must be the number", map.get("errorMessage"));
        assertEquals("2b", map.get("value"));
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus400AndErrorMessageWhenPageIsNotANumber() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port).concat("?page=1a")))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> map = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("'page' parameter must be the number", map.get("errorMessage"));
        assertEquals("1a", map.get("value"));
    }

    @Test
    @ClearDatabase
    @MigrateDatabase
    void shouldReturnStatus204WhenThereIsNoDataForGivenParameters() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port).concat("?page=100&offset=100")))
                .build();

        //when
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

}
