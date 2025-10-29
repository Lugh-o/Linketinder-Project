package com.acelerazg.controller

import com.acelerazg.dto.MatchDTO
import com.acelerazg.service.MatchService

class MatchController {
    private final MatchService matchService

    MatchController(MatchService matchService) {
        this.matchService = matchService
    }

    List<MatchDTO> handleGetAllMatchesByJobId(int idJob) {
        return matchService.getAllMatchesByJobId(idJob)
    }
}