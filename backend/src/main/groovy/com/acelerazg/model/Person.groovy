package com.acelerazg.model

import groovy.transform.CompileStatic

@CompileStatic
abstract class Person {
    int idPerson
    String email
    String description
    String passwd
    int idAddress

    // COMPLETO
    Person(int idPerson, String email, String description, String passwd, int idAddress) {
        this.idPerson = idPerson
        this.email = email
        this.description = description
        this.passwd = passwd
        this.idAddress = idAddress
    }


    // SEM idPerson, SEM passwd -- CREATE
    Person(String description, String passwd, String email) {
        this.description = description
        this.passwd = passwd
        this.email = email
    }

    // SEM SENHA -- READ
    Person(int idPerson, String email, String description, int idAddress) {
        this.idPerson = idPerson
        this.email = email
        this.description = description
        this.idAddress = idAddress
    }
}