package com.sumalukasz.testing.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Configuration
public class GlobalConfiguration {

    @Bean("employeeSimpleJdbcInsert")
    SimpleJdbcInsert employeeSimpleJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName("EMPLOYEE").usingGeneratedKeyColumns("ID");
    }
}
