package com.acelerazg.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class Job {
    int id
    String name
    String description
    int idAddress
    int idCompany

    // COMPLETO
    Job(int id, String name, String description, int idAddress, int idCompany) {
        this.id = id
        this.name = name
        this.description = description
        this.idAddress = idAddress
        this.idCompany = idCompany
    }

    // sem id
    Job(String name, String description, int idCompany) {
        this.name = name
        this.description = description
        this.idAddress = idAddress
        this.idCompany = idCompany
    }
}