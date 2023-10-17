package com.sumalukasz.testing.repository;

import com.sumalukasz.testing.model.request.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class InsertEmployeeRepository {

    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertEmployeeRepository.class);

    public InsertEmployeeRepository(@Qualifier("employeeSimpleJdbcInsert") SimpleJdbcInsert simpleJdbcInsert) {
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    public long insertEmployee(EmployeeRequest employeeRequest) {
        LOGGER.info("insertEmployee");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", employeeRequest.getName());
        params.addValue("surname", employeeRequest.getSurname());
        params.addValue("pesel", employeeRequest.getPesel());
        params.addValue("phoneNumber", employeeRequest.getPhoneNumber());
        params.addValue("dateOfBirth", employeeRequest.getDateOfBirth());
        params.addValue("departmentId", employeeRequest.getDepartmentId());
        Number number = simpleJdbcInsert.executeAndReturnKey(params);
        return number.longValue();
    }
}
