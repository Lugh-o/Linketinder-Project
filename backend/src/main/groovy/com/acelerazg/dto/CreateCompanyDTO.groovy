package com.acelerazg.dto

import com.acelerazg.model.Address
import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class CreateCompanyDTO {
    String description
    String passwd
    String email
    String name
    String cnpj
    Address address

    CreateCompanyDTO(String description, String passwd, String email, String name, String cnpj, Address address) {
        this.description = description
        this.passwd = passwd
        this.email = email
        this.name = name
        this.cnpj = cnpj
        this.address = address
    }
}
