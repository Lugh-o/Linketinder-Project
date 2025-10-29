package com.acelerazg.service

import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.dto.MatchDTO

class MatchService {
    private final MatchEventDAO matchEventDAO

    MatchService(MatchEventDAO matchEventDAO) {
        this.matchEventDAO = matchEventDAO
    }

    List<MatchDTO> getAllMatchesByJobId(int idJob) {
        return matchEventDAO.getAllMatchesByJobId(idJob)
    }
}