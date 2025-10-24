package com.acelerazg.model

import groovy.transform.CompileStatic

@CompileStatic
abstract class Person {
    int idPerson
    String email
    String description
    String passwd
    int idAddress

    Person(int idPerson, String email, String description, String passwd, int idAddress) {
        this.idPerson = idPerson
        this.email = email
        this.description = description
        this.passwd = passwd
        this.idAddress = idAddress
    }
}