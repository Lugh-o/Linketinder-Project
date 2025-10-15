package com.acelerazg.controller

import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dto.AnonymousCandidateDTO
import com.acelerazg.dto.CreateCandidateDTO
import com.acelerazg.model.Address
import com.acelerazg.model.Candidate
import groovy.transform.CompileStatic

@CompileStatic
class CandidateController {
    private final CandidateDAO candidateDAO

    CandidateController(CandidateDAO candidateDAO) {
        this.candidateDAO = candidateDAO
    }

    List<Candidate> handleGetAll() {
        return candidateDAO.getAll()
    }

    List<AnonymousCandidateDTO> handleGetAllInterestedInJob(int idJob) {
        return candidateDAO.getAllCandidatesInterestedByJobId(idJob)
    }

    Candidate handleCreateCandidate(CreateCandidateDTO createCandidateDTO) {

        if (candidateDAO.getByEmail(createCandidateDTO.email)) return null

        Candidate candidate = new Candidate(createCandidateDTO.description,
                createCandidateDTO.passwd,
                createCandidateDTO.email,
                createCandidateDTO.firstName,
                createCandidateDTO.lastName,
                createCandidateDTO.cpf,
                createCandidateDTO.birthday,
                createCandidateDTO.graduation)

        Address address = new Address(createCandidateDTO.address.state,
                createCandidateDTO.address.postalCode,
                createCandidateDTO.address.country,
                createCandidateDTO.address.city,
                createCandidateDTO.address.street)

        candidate = candidateDAO.create(candidate, address, createCandidateDTO.competencies)

        return candidate
    }

    boolean handleLikeJob(int idCandidate, int idJob) {
        if (candidateDAO.isJobAlreadyLiked(idCandidate, idJob)) return false
        candidateDAO.likeJob(idCandidate, idJob)
        return true
    }
}
