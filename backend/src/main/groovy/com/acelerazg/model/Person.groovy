package com.acelerazg.model


import groovy.transform.CompileStatic

@CompileStatic
abstract class Person {
    Integer idPerson
    String description
    String passwd
    String email
    Integer idAddress
    Address address
    Map originalMap = [:]

    Person(Integer idPerson, String description, String passwd, String email, Integer idAddress, Address address, Map originalMap) {
        this.idPerson = idPerson
        this.description = description
        this.passwd = passwd
        this.email = email
        this.idAddress = idAddress
        this.address = address
        this.originalMap = originalMap
    }

    boolean has(String key) {
        return originalMap?.containsKey(key) == true
    }
}
