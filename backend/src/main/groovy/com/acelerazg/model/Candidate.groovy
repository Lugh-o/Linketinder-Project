package com.acelerazg.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

import java.time.LocalDate

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class Candidate extends Person {
    int idCandidate
    String firstName
    String lastName
    String cpf
    LocalDate birthday
    String graduation
    int idPerson

    Candidate(int idPerson, String email, String description, String passwd, int idAddress, int idCandidate, String firstName, String lastName, String cpf, LocalDate birthday, String graduation) {
        super(idPerson, email, description, passwd, idAddress)
        this.idCandidate = idCandidate
        this.firstName = firstName
        this.lastName = lastName
        this.cpf = cpf
        this.birthday = birthday
        this.graduation = graduation
        this.idPerson = idPerson
    }

    // SEM SENHA
    Candidate(int idPerson, String email, String description, int idAddress, int idCandidate, String firstName, String lastName, String cpf, LocalDate birthday, String graduation) {
        super(idPerson, email, description, idAddress)
        this.idCandidate = idCandidate
        this.firstName = firstName
        this.lastName = lastName
        this.cpf = cpf
        this.birthday = birthday
        this.graduation = graduation
        this.idPerson = idPerson
    }

    // sem idPerson, sem idCandidate, sem idAddress, para o create
    Candidate(String description, String passwd, String email, String firstName, String lastName, String cpf, LocalDate birthday, String graduation) {
        super(description, passwd, email)
        this.firstName = firstName
        this.lastName = lastName
        this.cpf = cpf
        this.birthday = birthday
        this.graduation = graduation
    }
}