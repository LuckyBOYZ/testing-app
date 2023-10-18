package com.sumalukasz.testing.model.entity;

public record Address(
        String street,
        String houseNumber,
        String premisesNumber,
        String postcode,
        String city
) {
}
