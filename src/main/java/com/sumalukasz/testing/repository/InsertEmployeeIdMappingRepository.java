package com.sumalukasz.testing.repository;

import com.sumalukasz.testing.exception.InvalidAmountOfAddedRowsToDatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
        params.addValue("EMPLOYEE_ID", employeeId);
        params.addValue("UUID", uuid);
        int addedRows = simpleJdbcInsert.execute(params);
        if (addedRows != 1) {
            throw new InvalidAmountOfAddedRowsToDatabaseException(String.valueOf(addedRows), "Invalid amount of added rows with values: employeeId: %s, uuid: %s to Employee_ID_MAPPING table");
        }
        return uuid;
    }

}
