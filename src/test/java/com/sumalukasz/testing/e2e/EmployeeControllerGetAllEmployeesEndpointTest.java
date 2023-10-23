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
        String[] array = result.stream()
                .map(EmployeeDto::getUuid)
                .toArray(String[]::new);
        assertArrayEquals(new String[]{"4b77657e-6d9e-4dcf-8f73-da2528bee642", "64298930-ece6-4d41-87ea-5b983b92b0c8",
                "adcdc4f2-896a-4fcc-b2cc-0903f1acdc03", "2bf6fc06-15ff-4401-9d3b-d643b3859c23",
                "e66289d5-7078-4a00-b4f3-855d1af324f1", "f6b0e010-cb35-48a7-911a-be5e55846c2b",
                "aa39cea0-e918-4996-a3a8-6e37836b9dfa", "62853e83-b249-4be0-99fd-875cdb38404d",
                "40f14c7b-be35-441c-90b5-36b04d42d41b", "cd6e4c34-956f-4a49-957e-5bbfa2caffff"
        }, array);
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
        String[] array = result.stream()
                .map(EmployeeDto::getUuid)
                .toArray(String[]::new);
        String[] expected = {
                "4b77657e-6d9e-4dcf-8f73-da2528bee642", "64298930-ece6-4d41-87ea-5b983b92b0c8",
                "adcdc4f2-896a-4fcc-b2cc-0903f1acdc03", "2bf6fc06-15ff-4401-9d3b-d643b3859c23",
                "e66289d5-7078-4a00-b4f3-855d1af324f1", "f6b0e010-cb35-48a7-911a-be5e55846c2b",
                "aa39cea0-e918-4996-a3a8-6e37836b9dfa", "62853e83-b249-4be0-99fd-875cdb38404d",
                "40f14c7b-be35-441c-90b5-36b04d42d41b", "cd6e4c34-956f-4a49-957e-5bbfa2caffff",
                "c5eff1bf-4ee4-4422-bd44-b40bbfef9ce6", "5c3ef31e-d6df-47cd-b67e-1cd2b6037dfe",
                "54ea6586-cadc-4993-85af-5a491f04d457", "cfea9518-95e3-4891-bf7a-986244ab3a38",
                "7e019b8c-841d-4f22-a5d3-921ca6740fb7", "df48a26a-e949-46b5-9ad3-1a0bae799055",
                "5d6d23b5-d918-4917-a718-816b26c65c74", "4d314107-cf8b-4e1e-b022-ac3070f7d311",
                "16f8e0c0-19db-49f2-ba6f-1c212b63ea0a", "d9264c35-16ed-4815-b885-77c5033607c6",
                "afab8caf-9ccd-4ada-86a6-79ddb28acc4f", "f900481f-db1c-46e6-9b70-00dfa1035404",
                "4c20017d-1158-4f2d-aa22-9adb336e6110", "727ffec7-fe06-4828-9356-0c8678a61a72",
                "cfc39f5a-b9a6-4286-9fdb-c5b6719bf1c8", "681bcab2-2483-46f1-bb1b-ec4167bb2227",
                "4ca12b61-dced-48bd-8bc6-07abac6adef0", "7ec183ab-89c6-4ef5-b822-7fd5c9519aa9",
                "7657f661-94f4-4a47-8eb1-3a2ccbe08a2c", "05a7de40-7abb-4323-9817-9b0220af2e73"
        };
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
        String[] array = result.stream()
                .map(EmployeeDto::getUuid)
                .toArray(String[]::new);
        String[] expected = {
                "3a7cc758-0e45-48be-80d8-c0ae6ae8ea5b", "8b18fc06-fa02-48f3-850e-bf4bc8a12ce1",
                "3bd92ff3-aebe-477b-a6e5-534e426e4a94", "398041f9-9ecc-49c3-9aa5-66ac56524ae4",
                "bac79968-c9e8-4c4d-b9e2-b6f7864a9bb3", "ed190373-4ba8-4eb7-a2ce-88248f775b87",
                "a9de9323-e793-44fa-a609-0fab36e5bb3e", "4d9672bf-1aa8-4b0f-ad94-d7d8dd4a646a",
                "11d23297-e822-415d-bf2c-6fecbad8d062", "5eeed40f-a9ea-4773-9a2e-850fa2329944",
                "1bf01700-dafb-472d-a9ac-422b6d233deb", "a218fe03-b845-460d-90da-7dacd4af02a1",
                "4e1fa300-5753-462e-9311-f74c47998d85", "e9f4a6ef-6123-40d0-a0d5-f93a345564c4",
                "76ef1b6e-4a92-4ab8-8502-c71f0e2c8d22", "e7158763-1d7d-4c18-9112-8a334be056be",
                "15f8bcd9-9be7-4caa-be5b-582c85d4c5a4", "0deb2faa-ba72-4b71-bfe0-8c12839e244f",
                "d5cd6c46-859a-46c4-8b77-21b82216d527", "d9be4ccb-cc12-4085-8fbc-c156318c08da"
        };
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
