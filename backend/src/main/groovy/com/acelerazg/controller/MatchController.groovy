package com.acelerazg.controller

import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.dto.MatchDTO

class MatchController {
    private final MatchEventDAO matchEventDAO

    MatchController(MatchEventDAO matchEventDAO) {
        this.matchEventDAO = matchEventDAO
    }

    List<MatchDTO> handleGetAllMatchesByJobId(int idJob) {
        return matchEventDAO.getAllMatchesByJobId(idJob)
    }
}