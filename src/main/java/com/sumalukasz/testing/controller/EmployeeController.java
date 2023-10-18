package com.sumalukasz.testing.controller;

import com.sumalukasz.testing.constant.NumberPropertyNameConstant;
import com.sumalukasz.testing.exception.InvalidEmployeeIdException;
import com.sumalukasz.testing.exception.InvalidOffsetNumberException;
import com.sumalukasz.testing.exception.InvalidPageNumberException;
import com.sumalukasz.testing.model.dto.AddressDto;
import com.sumalukasz.testing.model.dto.EmployeeDto;
import com.sumalukasz.testing.model.request.EmployeeRequest;
import com.sumalukasz.testing.service.EmployeeService;
import com.sumalukasz.testing.utility.ConvertStringToIntegerUtils;
import com.sumalukasz.testing.utility.ConvertStringToLongUtils;
import com.sumalukasz.testing.utility.ValidateValuesUtils;
import com.sumalukasz.testing.utility.ValidateEmployeeRequestBodyUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(@RequestParam(value = "page", required = false, defaultValue = "1") String pageString,
                                                             @RequestParam(value = "offset", required = false, defaultValue = "10") String offsetString) {
        LOGGER.info("getAllEmployees|page={},offset={}", pageString, offsetString);
        int page = ConvertStringToIntegerUtils.convertStringToInt(pageString, NumberPropertyNameConstant.PAGE);
        if (page < 1) {
            throw new InvalidPageNumberException(pageString, "'page' parameter cannot be less than 1");
        }

        int offset = ConvertStringToIntegerUtils.convertStringToInt(offsetString, NumberPropertyNameConstant.OFFSET);
        if (offset < 1) {
            throw new InvalidOffsetNumberException(offsetString, "'offset' parameter cannot be less than 1");
        }

        List<EmployeeDto> employees = employeeService.getAllEmployees(page, offset);
        return ResponseEntity.ok(employees);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable(value = "id") String idString) {
        LOGGER.info("getEmployeeById|id={}", idString);
        long id = ConvertStringToLongUtils.convertStringToLong(idString, NumberPropertyNameConstant.ID);
        if (id < 1) {
            throw new InvalidEmployeeIdException(idString, "'id' path variable cannot be less than 1");
        }

        EmployeeDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDto> insertEmployee(@RequestBody EmployeeRequest employeeRequest,
                                                      HttpServletRequest request) {
        LOGGER.info("insertEmployee|employeeRequest={}", employeeRequest);
        ValidateEmployeeRequestBodyUtils.validateRequestBody(employeeRequest);
        long newEmployeeId = employeeService.insertEmployee(employeeRequest);
        return ResponseEntity
                .created(URI.create(request.getRequestURL().append(newEmployeeId).toString())).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteEmployee(@PathVariable(value = "id") String idString) {
        LOGGER.info("deleteEmployee|id={}", idString);
        long id = ConvertStringToLongUtils.convertStringToLong(idString, NumberPropertyNameConstant.ID);
        if (id < 1) {
            throw new InvalidEmployeeIdException(idString, "'id' path variable cannot be less than 1");
        }
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody Map<String, Object> requestBody,
                                         @PathVariable(value = "id") String idString) {
        LOGGER.info("updateEmployee|requestBody={},id={}", requestBody, idString);
        boolean areValuesNull = ValidateValuesUtils.areAllValuesInMapNull(requestBody);
        if (areValuesNull) {
            return ResponseEntity.noContent().build();
        }
        ValidateEmployeeRequestBodyUtils.validateRequestBodyByGivenMap(requestBody);
        long id = ConvertStringToLongUtils.convertStringToLong(idString, NumberPropertyNameConstant.ID);
        if (id < 1) {
            throw new InvalidEmployeeIdException(idString, "'id' path variable cannot be less than 1");
        }
        EmployeeDto updatedEmployee = employeeService.updateEmployee(requestBody, id);
        return ResponseEntity.ok(updatedEmployee);
    }

    @GetMapping(value = "/{id}/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getEmployeesAddressById(@PathVariable("id") String idString) {
        LOGGER.info("getEmployeesAddress|id={}", idString);
        long id = ConvertStringToLongUtils.convertStringToLong(idString, NumberPropertyNameConstant.ID);
        if (id < 1) {
            throw new InvalidEmployeeIdException(idString, "'id' path variable cannot be less than 1");
        }
        AddressDto addressDto = employeeService.getEmployeesAddressById(id);
        return ResponseEntity.ok(addressDto);
    }

}
