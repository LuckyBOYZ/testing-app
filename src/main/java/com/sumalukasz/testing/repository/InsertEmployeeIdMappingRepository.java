package com.sumalukasz.testing.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InsertEmployeeIdMappingRepository {

    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertEmployeeIdMappingRepository.class);

    public InsertEmployeeIdMappingRepository(@Qualifier("employeeIdMappingSimpleJdbcTemplate") SimpleJdbcInsert simpleJdbcInsert) {
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    public String insertEmployeeIdMapping(long employeeId, String uuid) {
        LOGGER.info("insertEmployeeIdMapping");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("employeeId", employeeId);
        params.addValue("uuid", uuid);
        KeyHolder keyHolder = simpleJdbcInsert.executeAndReturnKeyHolder(params);
        return keyHolder.getKeyAs(String.class);
    }

}
