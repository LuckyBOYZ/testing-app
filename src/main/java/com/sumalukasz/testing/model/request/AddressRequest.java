package com.sumalukasz.testing.model.request;

import com.sumalukasz.testing.model.entity.Address;

public final class AddressRequest {

    private final String street;
    private final String houseNumber;
    private final String premisesNumber;
    private final String postcode;
    private final String city;

    public AddressRequest(String street, String houseNumber, String premisesNumber, String postcode, String city) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.premisesNumber = premisesNumber;
        this.postcode = postcode;
        this.city = city;
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
