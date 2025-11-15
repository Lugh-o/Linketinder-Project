package com.acelerazg.dto

import com.acelerazg.model.Address
import com.acelerazg.model.Company
import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class CompanyDTO {
    String description
    String passwd
    String email
    String name
    String cnpj
    Address address
    Integer idPerson
    Integer idAddress
    Integer idCompany
    Map originalMap

    CompanyDTO(String description, String email, String name, String cnpj, Integer idPerson, Address address, Integer idAddress, Integer idCompany) {
        this.description = description
        this.email = email
        this.name = name
        this.cnpj = cnpj
        this.idPerson = idPerson
        this.address = address
        this.idAddress = idAddress
        this.idCompany = idCompany
        this.passwd = null
    }

    CompanyDTO(Map map) {
        this.originalMap = map
        this.description = map.description as String
        this.email = map.email as String
        this.name = map.name as String
        this.cnpj = map.cnpj as String
        this.idPerson = map.idPerson as Integer
        this.idAddress = map.idAddress as Integer
        this.idCompany = map.idCompany as Integer
        this.passwd = map.passwd as String

        def addressMap = map.address
        if (addressMap instanceof Map) {
            this.address = new Address(addressMap.state as String,
                    addressMap.postalCode as String,
                    addressMap.country as String,
                    addressMap.city as String,
                    addressMap.street as String)
        } else {
            this.address = null
        }
    }

    boolean has(String key) {
        return originalMap.containsKey(key)
    }

    Company toModel() {
        return new Company(
                this.idPerson,
                this.email,
                this.description,
                this.passwd,
                this.idAddress,
                this.idCompany,
                this.name,
                this.cnpj
        )
    }
}
