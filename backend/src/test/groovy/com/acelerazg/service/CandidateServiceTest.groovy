package com.acelerazg.service

import com.acelerazg.common.Response
import com.acelerazg.dao.AddressDAO
import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dao.CompetencyDAO
import com.acelerazg.dao.PersonDAO
import com.acelerazg.model.Address
import com.acelerazg.model.Candidate
import com.acelerazg.model.Competency
import spock.lang.Specification

import java.time.LocalDate

class CandidateServiceTest extends Specification {
    AddressDAO addressDAO
    PersonDAO personDAO
    CompetencyDAO competencyDAO
    CandidateDAO candidateDAO
    CandidateService candidateService

    def setup() {
        addressDAO = Mock()
        personDAO = Mock()
        competencyDAO = Mock()
        candidateDAO = Mock(CandidateDAO, constructorArgs: [addressDAO, personDAO, competencyDAO])
        candidateService = new CandidateService(candidateDAO)
    }

    def "CreateCandidate"() {
        given:
        Address address = Address.builder()
                .state("state")
                .postalCode("postal_code")
                .country("country")
                .street("street")
                .city("city")
                .build()
        List<Competency> competencies = [Competency.builder().name("Java").build(), Competency.builder().name("Python").build()]

        Candidate candidate = new Candidate(
                "description",
                "email",
                "firstName",
                "lastname",
                "cpf",
                LocalDate.of(2020,01,10),
                "graduation",
                address,
                competencies,
                1,
                1,
                null
        )

        Candidate created = new Candidate(
                "description",
                "email",
                "firstName",
                "lastname",
                "cpf",
                LocalDate.of(2020,01,10),
                "graduation",
                address,
                competencies,
                1,
                1,
                1
        )
        candidateDAO.create(candidate) >> created

        when:
        Response<Candidate> response = candidateService.createCandidate(candidate)

        then:
        response != null
        response.data.email == candidate.email
        response.data.firstName == candidate.firstName
        response.data.idCandidate != candidate.idCandidate
    }

    def "GetAllInterestedInJob"() {
        given:
        int idCandidate = 1
        int idJob = 42

        when:
        candidateService.likeJob(idCandidate, idJob)

        then:
        1 * candidateDAO.likeJob(idCandidate, idJob)
    }
}
