package com.sumalukasz.testing.model.dto;

import com.sumalukasz.testing.model.entity.Department;

public final class DepartmentDto {

    private final String name;

    private DepartmentDto(String name) {
        this.name = name;
    }

    public static DepartmentDto newDepartmentDto(Department department) {
        return new DepartmentDto(
                department.name()
        );
    }

    public String getName() {
        return name;
    }
}
