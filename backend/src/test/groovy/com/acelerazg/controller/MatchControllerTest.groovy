package com.acelerazg.controller

import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.dto.MatchDTO
import spock.lang.Specification

class MatchControllerTest extends Specification {
    MatchEventDAO matchEventDAO
    MatchController matchController

    def setup() {
        matchEventDAO = Mock()
        matchController = new MatchController(matchEventDAO)
    }

    def "HandleGetAllMatchesByJobId"() {
        given:
        int idJob = 2
        MatchDTO createdMatch1 = new MatchDTO('name', 'graduation')
        MatchDTO createdMatch2 = new MatchDTO('name', 'different graduation')
        matchEventDAO.getAllMatchesByJobId(idJob) >> [createdMatch1, createdMatch2]

        when:
        List<MatchDTO> result = matchEventDAO.getAllMatchesByJobId(idJob)

        then:
        result != null
        result.size() == 2
        result[1].candidateGraduation == 'different graduation'
    }
}