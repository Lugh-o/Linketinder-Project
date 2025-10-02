package com.acelerazg.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class Address {
    int id
    String state
    String postalCode
    String country
    String city
    String street

    Address(String street, String city, String country, String postalCode, String state, int id) {
        this.street = street
        this.city = city
        this.country = country
        this.postalCode = postalCode
        this.state = state
        this.id = id
    }

    Address(String state, String postalCode, String country, String city, String street) {
        this.state = state
        this.postalCode = postalCode
        this.country = country
        this.city = city
        this.street = street
    }
}