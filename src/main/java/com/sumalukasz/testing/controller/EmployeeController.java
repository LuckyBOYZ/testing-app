package com.sumalukasz.testing.controller;

import com.sumalukasz.testing.exception.InvalidOffsetNumberException;
import com.sumalukasz.testing.exception.InvalidPageNumberException;
import com.sumalukasz.testing.model.dto.EmployeeDto;
import com.sumalukasz.testing.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                             @RequestParam(required = false, defaultValue = "10") int offset) {
        LOGGER.info("getAllEmployees|pageNumber={},offset={}", pageNumber, offset);

        if (pageNumber < 1) {
            throw new InvalidPageNumberException(pageNumber, "Page number parameter cannot be less than 1");
        }

        if (offset < 1) {
            throw new InvalidOffsetNumberException(offset, "Page number parameter cannot be less than 1");
        }

        List<EmployeeDto> employees = employeeService.getAllEmployees(pageNumber, offset);
        return ResponseEntity.ok(employees);
    }

}
