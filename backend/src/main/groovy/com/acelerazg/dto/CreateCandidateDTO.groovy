package com.acelerazg.dto

import com.acelerazg.model.Address
import com.acelerazg.model.Competency
import groovy.transform.CompileStatic
import groovy.transform.ToString

import java.time.LocalDate

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class CreateCandidateDTO {
    String description
    String passwd
    String email
    String firstName
    String lastName
    String cpf
    LocalDate birthday
    String graduation
    Address address
    List<Competency> competencies

    CreateCandidateDTO(String description, String passwd, String email, String firstName, String lastName, String cpf, LocalDate birthday, String graduation, Address address, List<Competency> competencies) {
        this.description = description
        this.passwd = passwd
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        this.cpf = cpf
        this.birthday = birthday
        this.graduation = graduation
        this.address = address
        this.competencies = competencies
    }
}
