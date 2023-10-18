package com.sumalukasz.testing.model.dto;

import com.sumalukasz.testing.model.entity.Address;

public final class AddressDto {

    private final String street;
    private final String houseNumber;
    private final String premisesNumber;
    private final String postcode;
    private final String city;

    private AddressDto(String street, String houseNumber, String premisesNumber, String postcode, String city) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.premisesNumber = premisesNumber;
        this.postcode = postcode;
        this.city = city;
    }

    public static AddressDto newAddressDto(Address address) {
        return new AddressDto(
                address.street(),
                address.houseNumber(),
                address.premisesNumber(),
                address.postcode(),
                address.city()
        );
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPremisesNumber() {
        return premisesNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }
}
