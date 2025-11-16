package com.acelerazg.model


import groovy.transform.CompileStatic
import groovy.transform.ToString

import java.time.LocalDate

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class Candidate extends Person {
    Integer idCandidate
    String firstName
    String lastName
    String cpf
    LocalDate birthday
    String graduation
    List<Competency> competencies

    Candidate(String description, String email, String firstName, String lastName,
              String cpf, LocalDate birthday, String graduation,
              Address address, List<Competency> competencies,
              Integer idPerson, Integer idAddress, Integer idCandidate) {
        super(idPerson, description, null, email, idAddress, address, [:])

        this.firstName = firstName
        this.lastName = lastName
        this.cpf = cpf
        this.birthday = birthday
        this.graduation = graduation
        this.competencies = competencies ?: []
        this.idCandidate = idCandidate
    }

    Candidate(Map map) {
        super(map.idPerson as Integer,
                map.description as String,
                map.passwd as String,
                map.email as String,
                map.idAddress as Integer,
                null,
                map)

        this.firstName = map.firstName
        this.lastName = map.lastName
        this.cpf = map.cpf
        this.idCandidate = map.idCandidate as Integer
        this.birthday = map.birthday instanceof String ? LocalDate.parse(map.birthday as String) : map.birthday as LocalDate
        this.graduation = map.graduation

        def addressMap = map.address
        if (addressMap instanceof Map) {
            this.address = new Address(addressMap.get("state") as String,
                    addressMap.get("postalCode") as String,
                    addressMap.get("country") as String,
                    addressMap.get("city") as String,
                    addressMap.get("street") as String)
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