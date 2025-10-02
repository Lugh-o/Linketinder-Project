package com.acelerazg.controller

import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.dto.MatchDTO

class MatchController {
    static List<MatchDTO> handleGetAllMatchesByJobId(int idJob) {
        return MatchEventDAO.getAllMatchesByJobId(idJob)
    }
}
