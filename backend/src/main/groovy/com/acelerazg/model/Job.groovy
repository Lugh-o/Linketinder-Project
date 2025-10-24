package com.acelerazg.model

import com.acelerazg.model.builder.JobBuilder
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

    Job(int id, String name, String description, int idAddress, int idCompany) {
        this.id = id
        this.name = name
        this.description = description
        this.idAddress = idAddress
        this.idCompany = idCompany
    }

    static JobBuilder builder() {
        return new JobBuilder()
    }
}