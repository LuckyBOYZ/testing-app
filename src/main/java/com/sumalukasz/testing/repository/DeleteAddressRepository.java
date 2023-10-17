package com.sumalukasz.testing.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeleteAddressRepository {

    @Value("${delete.address}")
    private String query;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAddressRepository.class);

    public DeleteAddressRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteAddressByEmployeeId(long employeeId) {
        LOGGER.info("deleteAddressByEmployeeId");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("employeeId", employeeId);
        jdbcTemplate.update(query, params);
    }
}
