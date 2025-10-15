package com.acelerazg.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class Company extends Person {
    int idCompany
    String name
    String cnpj
    int idPerson

    // sem idPerson, sem idCompany, sem idAddress, para o create
    Company(String description, String passwd, String email, String name, String cnpj) {
        super(description, passwd, email)
        this.name = name
        this.cnpj = cnpj
        this.idPerson = idPerson
    }

    // Sem senha
    Company(int idPerson, String email, String description, int idAddress, int idCompany, String name, String cnpj) {
        super(idPerson, email, description, idAddress)
        this.idCompany = idCompany
        this.name = name
        this.cnpj = cnpj
        this.idPerson = idPerson
    }

    Company(int idPerson, String email, String description, String passwd, int idAddress, int idCompany, String name, String cnpj) {
        super(idPerson, email, description, passwd, idAddress)
        this.idCompany = idCompany
        this.name = name
        this.cnpj = cnpj
        this.idPerson = idPerson
    }
}