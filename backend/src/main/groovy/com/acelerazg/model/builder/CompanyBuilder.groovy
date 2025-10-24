package com.acelerazg.model.builder

import com.acelerazg.model.Company
import groovy.transform.CompileStatic

@CompileStatic
class CompanyBuilder {
    int idCompany
    String name
    String cnpj
    int idPerson
    String email
    String description
    String passwd
    int idAddress

    CompanyBuilder name(String name) {
        this.name = name
        return this
    }

    CompanyBuilder cnpj(String cnpj) {
        this.cnpj = cnpj
        return this
    }

    CompanyBuilder email(String email) {
        this.email = email
        return this
    }

    CompanyBuilder description(String description) {
        this.description = description
        return this
    }

    CompanyBuilder passwd(String passwd) {
        this.passwd = passwd
        return this
    }

    CompanyBuilder idPerson(int idPerson) {
        this.idPerson = idPerson
        return this
    }

    CompanyBuilder idCompany(int idCompany) {
        this.idCompany = idCompany
        return this
    }

    CompanyBuilder idAddress(int idAddress) {
        this.idAddress = idAddress
        return this
    }

    Company build() {
        return new Company(idPerson, email, description, passwd, idAddress, idCompany, name, cnpj)
    }
}
