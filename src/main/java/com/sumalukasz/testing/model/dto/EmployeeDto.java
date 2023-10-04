package com.sumalukasz.testing.model.dto;

import com.sumalukasz.testing.model.entity.Employee;

import java.time.LocalDate;

public final class EmployeeDto {

    private final Long id;
    private final String name;
    private final String surname;
    private final String pesel;
    private final String phoneNumber;
    private final LocalDate dateOfBirth;
    private final Long departmentId;

    private EmployeeDto(Long id, String name, String surname, String pesel, String phoneNumber, LocalDate dateOfBirth, Long departmentId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.departmentId = departmentId;
    }

    public static EmployeeDto newEmployeeDto(Employee employee) {
        return new EmployeeDto(
                employee.id(),
                employee.name(),
                employee.surname(),
                employee.pesel(),
                employee.phoneNumber(),
                employee.dateOfBirth(),
                employee.departmentId()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPesel() {
        return pesel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Long getDepartmentId() {
        return departmentId;
    }
}
