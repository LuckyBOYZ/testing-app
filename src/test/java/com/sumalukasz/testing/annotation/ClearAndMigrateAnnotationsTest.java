package com.sumalukasz.testing.annotation;

import com.sumalukasz.config.ClearDatabase;
import com.sumalukasz.config.IntegrationTest;
import com.sumalukasz.config.MigrateDatabase;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptStatementFailedException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.Connection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testing Clear and Migration Annotations")
@Tag("Annotation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClearAndMigrateAnnotationsTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Order(1)
    @MigrateDatabase
    void shouldAddAllDefaultDataToDatabase() {
        assertAll(
                () -> assertEquals(155, JdbcTestUtils.countRowsInTable(jdbcTemplate, "ADDRESS")),
                () -> assertEquals(6, JdbcTestUtils.countRowsInTable(jdbcTemplate, "DEPARTMENT")),
                () -> assertEquals(155, JdbcTestUtils.countRowsInTable(jdbcTemplate, "EMPLOYEE")),
                () -> assertEquals(17, JdbcTestUtils.countRowsInTable(jdbcTemplate, "SALES_EMPLOYEE")),
                () -> assertEquals(5, JdbcTestUtils.countRowsInTable(jdbcTemplate, "SALES_REGION"))
        );
    }

    @Test
    @Order(2)
    void shouldThrowScriptStatementFailedExceptionWhenScriptIsExecutedWithoutClearingDatabase() {
        ScriptStatementFailedException exception = assertThrows(
                ScriptStatementFailedException.class, () -> {
                    Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
                    ScriptUtils.executeSqlScript(connection, new ClassPathResource("sql/default_data_for_all_tables.sql"));
                }
        );
        assertTrue(exception.getCause().getMessage().startsWith("Duplicate entry"));
    }

    @Test
    @Order(3)
    @ClearDatabase
    void shouldBeEmptyAllTables() {
        assertAll(
                () -> assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "ADDRESS")),
                () -> assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "DEPARTMENT")),
                () -> assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "EMPLOYEE")),
                () -> assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "SALES_EMPLOYEE")),
                () -> assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "SALES_REGION"))
        );
    }

}
