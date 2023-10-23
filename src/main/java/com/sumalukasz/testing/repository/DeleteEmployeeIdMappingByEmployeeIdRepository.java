package com.sumalukasz.testing.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeleteEmployeeIdMappingByEmployeeIdRepository {

    @Value("${delete.employee.id.mapping.by.employee.id}")
    private String query;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteEmployeeIdMappingByEmployeeIdRepository.class);

    public DeleteEmployeeIdMappingByEmployeeIdRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteEmployeeIdMappingByEmployeeId(long employeeId) {
        LOGGER.info("deleteEmployeeIdMappingByEmployeeId");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("employeeId", employeeId);
        jdbcTemplate.update(query, params);
    }
}
