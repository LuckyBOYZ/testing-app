package com.sumalukasz.testing.repository;

import com.sumalukasz.testing.model.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GetEmployeeByIdRepository {

    @Value("${get.employee.by.id}")
    private String query;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(GetEmployeeByIdRepository.class);

    public GetEmployeeByIdRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Employee getEmployeeById(long id) {
        LOGGER.info("getEmployeeById");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", (id));
        return jdbcTemplate.queryForObject(query, params, new DataClassRowMapper<>(Employee.class));
    }
}
