package com.acelerazg.dto

import com.acelerazg.model.Address
import com.acelerazg.model.Competency
import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class CreateJobDTO {
    String name
    String description
    int idCompany
    Address address
    List<Competency> competencies

    CreateJobDTO(String name, String description, int idCompany, Address address, List<Competency> competencies) {
        this.name = name
        this.description = description
        this.idCompany = idCompany
        this.address = address
        this.competencies = competencies
    }
}
