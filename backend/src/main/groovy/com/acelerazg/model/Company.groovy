package com.acelerazg.model


import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class Company extends Person {
    Integer idCompany
    String name
    String cnpj

    Company(String description, String email, String name, String cnpj, Integer idPerson, Address address, Integer idAddress, Integer idCompany) {
        super(idPerson, description, null, email, idAddress, address, [:])
        this.idCompany = idCompany
        this.name = name
        this.cnpj = cnpj
    }

    Company(Map map) {
        super(map.idPerson as Integer,
                map.description as String,
                map.passwd as String,
                map.email as String,
                map.idAddress as Integer,
                null,
                map)
        this.name = map.name as String
        this.cnpj = map.cnpj as String
        this.idCompany = map.idCompany as Integer

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
}
