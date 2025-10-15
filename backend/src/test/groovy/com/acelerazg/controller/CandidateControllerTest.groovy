package com.acelerazg.controller

import com.acelerazg.dao.AddressDAO
import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dao.CompetencyDAO
import com.acelerazg.dao.PersonDAO
import com.acelerazg.dto.AnonymousCandidateDTO
import com.acelerazg.dto.CreateCandidateDTO
import com.acelerazg.model.Address
import com.acelerazg.model.Candidate
import com.acelerazg.model.Competency
import spock.lang.Specification

import java.time.LocalDate

class CandidateControllerTest extends Specification {
    AddressDAO addressDAO
    PersonDAO personDAO
    CompetencyDAO competencyDAO
    CandidateDAO candidateDAO
    CandidateController controller

    def setup() {
        addressDAO = Mock()
        personDAO = Mock()
        competencyDAO = Mock()
        candidateDAO = Mock(CandidateDAO, constructorArgs: [addressDAO, personDAO, competencyDAO])
        controller = new CandidateController(candidateDAO)
    }

    def "HandleGetAll"() {
        given:
        Candidate fake1 = new Candidate(1, "mail", "desc", 1, 1,
                "First", "Last", "123", LocalDate.now(), "CS")
        Candidate fake2 = new Candidate(1, "mail", "desc", 1, 1,
                "First", "Last", "123", LocalDate.now(), "CS")
        candidateDAO.getAll() >> [fake1, fake2]

        when:
        List<Candidate> candidateList = controller.handleGetAll()

        then:
        candidateList.size() == 2
        candidateList[0].firstName == "First"
    }

    def "HandleGetAllInterestedInJob"() {
        given:
        AnonymousCandidateDTO fakeDTO = new AnonymousCandidateDTO(1,
                "CS",
                "Description",
                [new Competency("Java"), new Competency("Python")])

        candidateDAO.getAllCandidatesInterestedByJobId(42) >> [fakeDTO]

        when:
        List<AnonymousCandidateDTO> result = controller.handleGetAllInterestedInJob(42)

        then:
        result.size() == 1
        result[0].id == 1
        result[0].competencies*.name.containsAll(["Java", "Python"])
    }

    def "HandleCreateCandidate"() {
        given:
        String email = "test@mail.com"
        List<Competency> competencies = [new Competency("Java"), new Competency("Python")]

        Candidate createdCandidate = new Candidate(1, email, "desc", 1, 1, "First", "Last", "123", LocalDate.now(), "CS")
        Address createdCandidateAddress = new Address("State", "12345", "Country", "City", "Street")
        CreateCandidateDTO createCandidateDTO = new CreateCandidateDTO("desc", "pass", email, "First", "Last", "123",
                LocalDate.now(), "CS", createdCandidateAddress, competencies)


        candidateDAO.getByEmail(email) >> null
        candidateDAO.create(*_) >> createdCandidate

        when:
        Candidate result = controller.handleCreateCandidate(createCandidateDTO)

        then:
        result != null
        result.email == email
        result.firstName == "First"
    }

    def "HandleLikeJob"() {
        given:
        int idCandidate = 1
        int idJob = 42

        when:
        controller.handleLikeJob(idCandidate, idJob)

        then:
        1 * candidateDAO.likeJob(idCandidate, idJob)
    }
}