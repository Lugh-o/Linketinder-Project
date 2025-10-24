package com.acelerazg.model.builder

import com.acelerazg.model.Candidate
import groovy.transform.CompileStatic

import java.time.LocalDate

@CompileStatic
class CandidateBuilder {
    int idCandidate
    String firstName
    String lastName
    String cpf
    LocalDate birthday
    String graduation
    int idPerson
    String email
    String description
    String passwd
    int idAddress

    CandidateBuilder idCandidate(int idCandidate) {
        this.idCandidate = idCandidate
        return this
    }

    CandidateBuilder firstName(String firstName) {
        this.firstName = firstName
        return this
    }

    CandidateBuilder lastName(String lastName) {
        this.lastName = lastName
        return this
    }

    CandidateBuilder cpf(String cpf) {
        this.cpf = cpf
        return this
    }

    CandidateBuilder birthday(LocalDate birthday) {
        this.birthday = birthday
        return this
    }

    CandidateBuilder graduation(String graduation) {
        this.graduation = graduation
        return this
    }

    CandidateBuilder idPerson(int idPerson) {
        this.idPerson = idPerson
        return this
    }

    CandidateBuilder email(String email) {
        this.email = email
        return this
    }

    CandidateBuilder description(String description) {
        this.description = description
        return this
    }

    CandidateBuilder passwd(String passwd) {
        this.passwd = passwd
        return this
    }

    CandidateBuilder idAddress(int idAddress) {
        this.idAddress = idAddress
        return this
    }

    Candidate build() {
        return new Candidate(idPerson, email, description, passwd, idAddress, idCandidate, firstName, lastName, cpf, birthday, graduation)
    }

}
