package com.sumalukasz.testing.repository;

import com.sumalukasz.testing.model.entity.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GetDepartmentByIdRepository {

    @Value("${get.department.by.id}")
    private String query;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(GetDepartmentByIdRepository.class);

    public GetDepartmentByIdRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Department getDepartmentById(long id) {
        LOGGER.info("getDepartmentById");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", (id));
        try {
            return jdbcTemplate.queryForObject(query, params, new DataClassRowMapper<>(Department.class));
        } catch (EmptyResultDataAccessException ignore) {
            return null;
        }
    }
}
