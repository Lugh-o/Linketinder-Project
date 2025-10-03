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
    private final CandidateDAO candidateDao

    CandidateController(CandidateDAO candidateDao) {
        this.candidateDao = candidateDao
    }

    List<Candidate> handleGetAll() {
        return candidateDao.getAll()
    }

    List<AnonymousCandidateDTO> handleGetAllInterestedInJob(int idJob) {
        return candidateDao.getAllCandidatesInterestedByJobId(idJob)
    }

    Candidate handleCreateCandidate(String description, String passwd, String email, String firstName, String lastName, String cpf, LocalDate birthday, String graduation, String state, String postalCode, String country, String city, String street, List<Competency> competencies) {

        if (candidateDao.getByEmail(email)) return null

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

        candidate = candidateDao.create(candidate, address, competencies)

        return candidate
    }

    boolean handleLikeJob(int idCandidate, int idJob) {
        if (candidateDao.isJobAlreadyLiked(idCandidate, idJob)) return false
        candidateDao.likeJob(idCandidate, idJob)
        return true
    }
}
