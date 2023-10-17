package com.sumalukasz.testing.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeleteEmployeeRepository {

    @Value("${delete.employee}")
    private String query;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteEmployeeRepository.class);

    public DeleteEmployeeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteEmployee(long id) {
        LOGGER.info("deleteEmployee");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(query, params);
    }
}
