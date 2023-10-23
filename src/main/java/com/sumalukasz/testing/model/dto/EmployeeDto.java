package com.sumalukasz.testing.model.dto;

import com.sumalukasz.testing.model.entity.Employee;

import java.time.LocalDate;

public final class EmployeeDto {

    private final String uuid;
    private final String name;
    private final String surname;
    private final String pesel;
    private final String phoneNumber;
    private final LocalDate dateOfBirth;
    private final Long departmentId;

    private EmployeeDto(String uuid, String name, String surname, String pesel, String phoneNumber, LocalDate dateOfBirth, Long departmentId) {
        this.uuid = uuid;
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.departmentId = departmentId;
    }

    public static EmployeeDto newEmployeeDto(Employee employee, boolean withId) {
        return new EmployeeDto(
                withId ? employee.uuid() : null,
                employee.name(),
                employee.surname(),
                employee.pesel(),
                employee.phoneNumber(),
                employee.dateOfBirth(),
                employee.departmentId()
        );
    }

    public String getUuid() {
        return uuid;
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
