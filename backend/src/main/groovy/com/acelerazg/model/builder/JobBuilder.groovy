package com.acelerazg.model.builder

import com.acelerazg.model.Job
import groovy.transform.CompileStatic

@CompileStatic
class JobBuilder {
    int id
    String name
    String description
    int idAddress
    int idCompany

    JobBuilder id(int id) {
        this.id = id
        return this
    }

    JobBuilder name(String name) {
        this.name = name
        return this
    }

    JobBuilder description(String description) {
        this.description = description
        return this
    }

    JobBuilder idAddress(int idAddress) {
        this.idAddress = idAddress
        return this
    }

    JobBuilder idCompany(int idCompany) {
        this.idCompany = idCompany
        return this
    }

    Job build() {
        return new Job(id, name, description, idAddress, idCompany)
    }
}
