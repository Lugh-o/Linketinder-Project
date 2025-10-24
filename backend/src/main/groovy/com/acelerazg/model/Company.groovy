package com.acelerazg.model

import com.acelerazg.model.builder.CompanyBuilder
import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class Company extends Person {
    int idCompany
    String name
    String cnpj
    int idPerson

    Company(int idPerson, String email, String description, String passwd, int idAddress, int idCompany, String name, String cnpj) {
        super(idPerson, email, description, passwd, idAddress)
        this.idCompany = idCompany
        this.name = name
        this.cnpj = cnpj
        this.idPerson = idPerson
    }

    static CompanyBuilder builder(){
        return new CompanyBuilder()
    }
}