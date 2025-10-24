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

        Candidate candidate = Candidate.builder()
                .description(createCandidateDTO.description)
                .passwd(createCandidateDTO.passwd)
                .email(createCandidateDTO.email)
                .firstName(createCandidateDTO.firstName)
                .lastName(createCandidateDTO.lastName)
                .cpf(createCandidateDTO.cpf)
                .birthday(createCandidateDTO.birthday)
                .graduation(createCandidateDTO.graduation)
                .build()

        Address address = Address.builder()
                .state(createCandidateDTO.address.state)
                .postalCode(createCandidateDTO.address.postalCode)
                .country(createCandidateDTO.address.country)
                .city(createCandidateDTO.address.city)
                .street(createCandidateDTO.address.street)
                .build()

        candidate = candidateDAO.create(candidate, address, createCandidateDTO.competencies)

        return candidate
    }

    boolean handleLikeJob(int idCandidate, int idJob) {
        if (candidateDAO.isJobAlreadyLiked(idCandidate, idJob)) return false
        candidateDAO.likeJob(idCandidate, idJob)
        return true
    }
}
