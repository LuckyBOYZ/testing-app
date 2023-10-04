package com.sumalukasz.testing.model.entity;

import java.time.LocalDate;

public record Employee(
        Long id,
        String name,
        String surname,
        String pesel,
        String phoneNumber,
        LocalDate dateOfBirth,
        Long departmentId

) {
}
