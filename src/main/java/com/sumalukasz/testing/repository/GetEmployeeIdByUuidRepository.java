package com.sumalukasz.testing.repository;

import com.sumalukasz.testing.exception.InvalidUuidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GetEmployeeIdByUuidRepository {

    @Value("${get.employee.id.by.uuid}")
    private String query;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(GetEmployeeIdByUuidRepository.class);

    public GetEmployeeIdByUuidRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("DataFlowIssue")
    public long getEmployeeIdByUuid(String uuid) {
        LOGGER.info("getEmployeeIdByUuid");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("uuid", uuid);
        try {
            return jdbcTemplate.queryForObject(query, params, Long.class);
        } catch (EmptyResultDataAccessException ignore) {
            throw new InvalidUuidException(uuid, "Uuid for employee_id doesn't exist in database");
        }
    }
}
