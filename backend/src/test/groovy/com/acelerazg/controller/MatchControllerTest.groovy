package com.acelerazg.controller

import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.dto.MatchDTO
import com.acelerazg.service.MatchService
import spock.lang.Specification

class MatchControllerTest extends Specification {
    MatchEventDAO matchEventDAO
    MatchService matchService
    MatchController matchController

    def setup() {
        matchEventDAO = Mock()
        matchService = Mock(MatchService, constructorArgs: [matchEventDAO])
        matchController = new MatchController(matchService)
    }

    def "HandleGetAllMatchesByJobId"() {
        given:
        int idJob = 2
        MatchDTO createdMatch1 = new MatchDTO('name', 'graduation')
        MatchDTO createdMatch2 = new MatchDTO('name', 'different graduation')
        matchService.getAllMatchesByJobId(idJob) >> [createdMatch1, createdMatch2]

        when:
        List<MatchDTO> result = matchController.handleGetAllMatchesByJobId(idJob)

        then:
        result != null
        result.size() == 2
        result[1].candidateGraduation == 'different graduation'
    }
}