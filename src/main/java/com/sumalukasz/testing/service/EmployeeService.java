package com.sumalukasz.testing.service;

import com.sumalukasz.testing.exception.InvalidDepartmentIdException;
import com.sumalukasz.testing.model.dto.EmployeeDto;
import com.sumalukasz.testing.model.entity.Department;
import com.sumalukasz.testing.model.entity.Employee;
import com.sumalukasz.testing.model.request.EmployeeRequest;
import com.sumalukasz.testing.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private final GetAllEmployeesRepository getAllEmployeesRepository;
    private final GetEmployeeByIdRepository getEmployeeByIdRepository;
    private final InsertEmployeeRepository insertEmployeeRepository;
    private final DeleteEmployeeRepository deleteEmployeeRepository;
    private final DeleteAddressRepository deleteAddressRepository;
    private final GetDepartmentByIdRepository getDepartmentByIdRepository;
    private final UpdateEmployeeRepository updateEmployeeRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(GetAllEmployeesRepository getAllEmployeesRepository, GetEmployeeByIdRepository getEmployeeByIdRepository, InsertEmployeeRepository insertEmployeeRepository, DeleteEmployeeRepository deleteEmployeeRepository, DeleteAddressRepository deleteAddressRepository, GetDepartmentByIdRepository getDepartmentByIdRepository, UpdateEmployeeRepository updateEmployeeRepository) {
        this.getAllEmployeesRepository = getAllEmployeesRepository;
        this.getEmployeeByIdRepository = getEmployeeByIdRepository;
        this.insertEmployeeRepository = insertEmployeeRepository;
        this.deleteEmployeeRepository = deleteEmployeeRepository;
        this.deleteAddressRepository = deleteAddressRepository;
        this.getDepartmentByIdRepository = getDepartmentByIdRepository;
        this.updateEmployeeRepository = updateEmployeeRepository;
    }

    public List<EmployeeDto> getAllEmployees(int page, int offset) {
        LOGGER.info("getAllEmployees");
        List<Employee> result = getAllEmployeesRepository.getAllEmployees(page, offset);
        if (result.isEmpty()) {
            throw new EmptyResultDataAccessException(offset);
        }
        return result.stream()
                .map(el -> EmployeeDto.newEmployeeDto(el, true))
                .toList();
    }

    public EmployeeDto getEmployeeById(long id) {
        LOGGER.info("getAllEmployees");
        Employee result = getEmployeeByIdRepository.getEmployeeById(id);
        return EmployeeDto.newEmployeeDto(result, true);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public long insertEmployee(EmployeeRequest employeeRequest) {
        LOGGER.info("insertEmployee");
        if (employeeRequest.getDepartmentId() != null) {
            Department department = getDepartmentByIdRepository.getDepartmentById(employeeRequest.getDepartmentId());
            if (department == null) {
                throw new InvalidDepartmentIdException(String.valueOf(employeeRequest.getDepartmentId()), "No department for given id");
            }
        }
        return insertEmployeeRepository.insertEmployee(employeeRequest);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteEmployee(long id) {
        LOGGER.info("deleteEmployee");
        deleteAddressRepository.deleteAddressByEmployeeId(id);
        deleteEmployeeRepository.deleteEmployee(id);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public EmployeeDto updateEmployee(Map<String, Object> requestBody, long id) {
        LOGGER.info("updateEmployee");
        updateEmployeeRepository.updateEmployee(requestBody, id);
        Employee result = getEmployeeByIdRepository.getEmployeeById(id);
        return EmployeeDto.newEmployeeDto(result, false);
    }
}
