package com.sumalukasz.testing.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Configuration
public class GlobalConfiguration {

    @Bean("employeeSimpleJdbcTemplate")
    SimpleJdbcInsert employeeSimpleJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName("EMPLOYEE").usingGeneratedKeyColumns("ID");
    }

    @Bean("employeeIdMappingSimpleJdbcTemplate")
    SimpleJdbcInsert employeeIdMappingSimpleJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName("EMPLOYEE_ID_MAPPING").usingGeneratedKeyColumns("UUID");
    }
}
