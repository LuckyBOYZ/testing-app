package com.sumalukasz.testing.repository;

import com.sumalukasz.testing.utility.ConvertingCamelCaseToSnakeUpperCaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UpdateEmployeeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateEmployeeRepository.class);

    public UpdateEmployeeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("SqlSourceToSinkFlow")
    public void updateEmployee(Map<String, Object> requestBody, long id) {
        LOGGER.info("updateEmployee");
        StringBuilder queryBuilder = new StringBuilder("UPDATE EMPLOYEE SET ");
        MapSqlParameterSource params = new MapSqlParameterSource();
        prepareSqlByGivenRequestBody(requestBody, queryBuilder, params);
        int length = queryBuilder.length();
        queryBuilder.replace(length - 1, length, " ");
        queryBuilder.append("WHERE ID = :id");
        params.addValue("id", id);
        jdbcTemplate.update(queryBuilder.toString(), params);
    }

    private void prepareSqlByGivenRequestBody(Map<String, Object> requestBody, StringBuilder queryBuilder, MapSqlParameterSource params) {
        for (Map.Entry<String, Object> fieldToUpdate : requestBody.entrySet()) {
            String key = fieldToUpdate.getKey();
            Object value = fieldToUpdate.getValue();
            String keyLowerCase = key.toLowerCase();
            String queryStatement = "%s = :%s,";
            String sneakUppercaseKey = ConvertingCamelCaseToSnakeUpperCaseUtils.convertCamelCaseStringToSnakeUppercase(key);
            queryBuilder.append(queryStatement.formatted(sneakUppercaseKey, keyLowerCase));
            params.addValue(keyLowerCase, value);
        }
    }
}
