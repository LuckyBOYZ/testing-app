package com.sumalukasz.testing.repository;

import com.sumalukasz.testing.model.entity.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GetEmployeesAddressByEmployeeIdRepository {

    @Value("${get.employees.address.by.id}")
    private String query;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(GetEmployeesAddressByEmployeeIdRepository.class);

    public GetEmployeesAddressByEmployeeIdRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Address getEmployeesAddressByEmployeeId(long employeeId) {
        LOGGER.info("getEmployeesAddressByEmployeeId");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("employeeId", employeeId);
        try {
            return jdbcTemplate.queryForObject(query, params, new DataClassRowMapper<>(Address.class));
        } catch (EmptyResultDataAccessException ignore) {
            return null;
        }
    }
}
