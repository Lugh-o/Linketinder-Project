package com.acelerazg.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class Address {
    String state
    String postalCode
    String country
    String city
    String street

    Address(String state, String postalCode, String country, String city, String street) {
        this.state = state
        this.postalCode = postalCode
        this.country = country
        this.city = city
        this.street = street
    }
}