package com.sumalukasz.testing.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumalukasz.config.*;
import com.sumalukasz.testing.model.dto.EmployeeDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@DisplayName("Testing Employee Controller")
@Tag("Controller")
@Import(TestBeansConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@EnabledIfSystemProperty(named = "spring.profiles.active", matches = "test")
class EmployeeControllerGetAllEmployeesEndpointTest extends IntegrationTest {

    private static final String BASIC_URL = "http://localhost:%s/employee";

    @LocalServerPort
    private int port;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @ClearDatabase
    @MigrateDatabase
    void shouldReturn10RowsWhenEndpointDoesntHaveQueryParameters() throws IOException, InterruptedException {
        // given
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASIC_URL.formatted(port)))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        //when
        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        String body = res.body();
        List<EmployeeDto> result = objectMapper.readValue(body, new TypeReference<>() {
        });

        //then
        assertEquals(10, result.size());

    }

    @Test
    @Order(2)
    @ClearDatabase
    void shouldReturn0RowsWhenDatabaseHasNoRecords() {
        assertThat(0).isEqualTo(JdbcTestUtils.countRowsInTable(jdbcTemplate, "SALES_REGION"));
    }

    @Test
    @Order(3)
    @MigrateDatabase
    void shouldReturn() {
        assertThat(0).isEqualTo(JdbcTestUtils.countRowsInTable(jdbcTemplate, "SALES_REGION"));
    }
}
