package com.acelerazg.controller

import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dto.AnonymousCandidateDTO
import com.acelerazg.model.Address
import com.acelerazg.model.Candidate
import com.acelerazg.model.Competency
import groovy.transform.CompileStatic

import java.time.LocalDate

@CompileStatic
class CandidateController {
    static List<Candidate> handleGetAll() {
        return CandidateDAO.getAll()
    }

    static List<AnonymousCandidateDTO> handleGetAllInterestedInJob(int idJob) {
        return CandidateDAO.getAllCandidatesInterestedByJobId(idJob)
    }

    static Candidate handleCreateCandidate(String description, String passwd, String email, String firstName, String lastName, String cpf, LocalDate birthday, String graduation, String state, String postalCode, String country, String city, String street, List<Competency> competencies) {

        if (CandidateDAO.getByEmail(email)) return null

        Candidate candidate = new Candidate(
                description,
                passwd,
                email,
                firstName,
                lastName,
                cpf,
                birthday,
                graduation
        )

        Address address = new Address(
                state,
                postalCode,
                country,
                city,
                street
        )

        candidate = CandidateDAO.create(candidate, address, competencies)

        return candidate
    }

    static void handleLikeJob(int idCandidate, int idJob) {
        CandidateDAO.likeJob(idCandidate, idJob)
    }
}
