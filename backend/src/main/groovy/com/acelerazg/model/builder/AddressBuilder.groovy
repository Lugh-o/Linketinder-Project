package com.acelerazg.model.builder

import com.acelerazg.model.Address
import groovy.transform.CompileStatic

@CompileStatic
class AddressBuilder {
    String state
    String postalCode
    String country
    String city
    String street

    AddressBuilder state(String state) {
        this.state = state
        return this
    }

    AddressBuilder postalCode(String postalCode) {
        this.postalCode = postalCode
        return this
    }

    AddressBuilder country(String country) {
        this.country = country
        return this
    }

    AddressBuilder city(String city) {
        this.city = city
        return this
    }

    AddressBuilder street(String street) {
        this.street = street
        return this
    }

    Address build() {
        return new Address(state, postalCode, country, city, street)
    }
}
