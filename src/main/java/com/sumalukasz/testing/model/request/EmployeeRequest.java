package com.sumalukasz.testing.model.request;

import java.time.LocalDate;

public final class EmployeeRequest {

    private final String name;
    private final String surname;
    private final String pesel;
    private final String phoneNumber;
    private final LocalDate dateOfBirth;
    private final Long departmentId;

    public EmployeeRequest(String name, String surname, String pesel, String phoneNumber, LocalDate dateOfBirth, Long departmentId) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.departmentId = departmentId;
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
