package com.acelerazg.service

import com.acelerazg.common.Response
import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.dto.MatchDTO

import javax.servlet.http.HttpServletResponse

class MatchService {
    private final MatchEventDAO matchEventDAO

    MatchService(MatchEventDAO matchEventDAO) {
        this.matchEventDAO = matchEventDAO
    }

    Response<List<MatchDTO>> getAllMatchesByJobId(int idJob) {
        List<MatchDTO> matchList = matchEventDAO.getAllMatchesByJobId(idJob)
        if (matchList == []) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND, "This job has no matches.")
        }
        return Response.success(HttpServletResponse.SC_OK, matchList)
    }
}