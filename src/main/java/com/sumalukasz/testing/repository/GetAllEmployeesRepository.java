package com.sumalukasz.testing.repository;

import com.sumalukasz.testing.model.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GetAllEmployeesRepository {

    @Value("${get.all.employees}")
    private String query;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAllEmployeesRepository.class);

    public GetAllEmployeesRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> getAllEmployees(int pageNumber, int offset) {
        LOGGER.info("getAllEmployees");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("offset", (pageNumber - 1) * offset);
        params.addValue("numOfRecords", offset);
        return jdbcTemplate.query(query, params, new DataClassRowMapper<>(Employee.class));
    }
}
