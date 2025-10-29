package com.acelerazg.controller

import com.acelerazg.dto.AnonymousCandidateDTO
import com.acelerazg.dto.CreateCandidateDTO
import com.acelerazg.model.Candidate
import com.acelerazg.service.CandidateService
import groovy.transform.CompileStatic

@CompileStatic
class CandidateController {
    private final CandidateService candidateService

    CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService
    }

    List<Candidate> handleGetAll() {
        return candidateService.getAllCandidates()
    }

    List<AnonymousCandidateDTO> handleGetAllInterestedInJob(int idJob) {
        return candidateService.getAllInterestedInJob(idJob)
    }

    Candidate handleCreateCandidate(CreateCandidateDTO createCandidateDTO) {
        return candidateService.createCandidate(createCandidateDTO)
    }

    boolean handleLikeJob(int idCandidate, int idJob) {
        return candidateService.likeJob(idCandidate, idJob)
    }
}
