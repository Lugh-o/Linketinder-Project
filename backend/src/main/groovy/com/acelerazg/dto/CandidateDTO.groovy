package com.acelerazg.dto

import com.acelerazg.model.Address
import com.acelerazg.model.Competency
import groovy.transform.CompileStatic
import groovy.transform.ToString

import java.time.LocalDate

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class CandidateDTO {
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
    Integer idPerson
    Integer idAddress
    Integer idCandidate
    Map originalMap

    CandidateDTO(String description, String email, String firstName, String lastName,
                 String cpf, LocalDate birthday, String graduation,
                 Address address, List<Competency> competencies,
                 Integer idPerson, Integer idAddress, Integer idCandidate) {
        this.description = description
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        this.cpf = cpf
        this.birthday = birthday
        this.graduation = graduation
        this.address = address
        this.competencies = competencies ?: []
        this.idPerson = idPerson
        this.idAddress = idAddress
        this.idCandidate = idCandidate
        this.passwd = null
    }

    CandidateDTO(Map map) {
        this.originalMap = map
        this.description = map.description
        this.passwd = map.passwd
        this.email = map.email
        this.firstName = map.firstName
        this.lastName = map.lastName
        this.cpf = map.cpf
        this.idPerson = map.idPerson as Integer
        this.idAddress = map.idAddress as Integer
        this.idCandidate = map.idCandidate as Integer

        this.birthday = map.birthday instanceof String ? LocalDate.parse(map.birthday as String) : map.birthday as LocalDate

        this.graduation = map.graduation

        Address address = map.get("address") as Address
        if (address instanceof Map) {
            this.address = new Address(address.get("state") as String,
                    address.get("postalCode") as String,
                    address.get("country") as String,
                    address.get("city") as String,
                    address.get("street") as String)
        } else {
            this.address = null
        }

        this.competencies = map.competencies instanceof List ? map.competencies.collect { Object name ->
            Competency.builder()
                    .name(name as String)
                    .build()
        } : []

    }

    boolean has(String key) {
        return originalMap.containsKey(key)
    }
}
