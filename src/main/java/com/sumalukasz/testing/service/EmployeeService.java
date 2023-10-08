package com.sumalukasz.testing.service;

import com.sumalukasz.testing.model.dto.EmployeeDto;
import com.sumalukasz.testing.model.entity.Employee;
import com.sumalukasz.testing.repository.GetAllEmployeesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final GetAllEmployeesRepository getAllEmployeesRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(GetAllEmployeesRepository getAllEmployeesRepository) {
        this.getAllEmployeesRepository = getAllEmployeesRepository;
    }

    public List<EmployeeDto> getAllEmployees(int page, int offset) {
        LOGGER.info("getAllEmployees");
        List<Employee> result = getAllEmployeesRepository.getAllEmployees(page, offset);
        return result.stream()
                .map(EmployeeDto::newEmployeeDto)
                .toList();
    }

}
