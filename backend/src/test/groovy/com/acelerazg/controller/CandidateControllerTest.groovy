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
import com.acelerazg.service.CandidateService
import spock.lang.Specification

import java.time.LocalDate

class CandidateControllerTest extends Specification {
    AddressDAO addressDAO
    PersonDAO personDAO
    CompetencyDAO competencyDAO
    CandidateService candidateService
    CandidateDAO candidateDAO
    CandidateController controller

    def setup() {
        addressDAO = Mock()
        personDAO = Mock()
        competencyDAO = Mock()
        candidateDAO = Mock(CandidateDAO, constructorArgs: [addressDAO, personDAO, competencyDAO])
        candidateService = Mock(CandidateService, constructorArgs: [candidateDAO])
        controller = new CandidateController(candidateService)
    }

    def "HandleGetAll"() {
        given:
        Candidate fake1 = Candidate.builder()
                .idPerson(1)
                .email("mail")
                .description("desc")
                .idAddress(1)
                .idCandidate(1)
                .firstName("First")
                .lastName("Last")
                .cpf("123")
                .birthday(LocalDate.now())
                .graduation("CS")
                .build()

        Candidate fake2 = Candidate.builder()
                .idPerson(1)
                .email("mail")
                .description("desc")
                .idAddress(1)
                .idCandidate(1)
                .firstName("First")
                .lastName("Last")
                .cpf("123")
                .birthday(LocalDate.now())
                .graduation("CS")
                .build()

        candidateService.getAllCandidates() >> [fake1, fake2]

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
                [Competency.builder().name("Java").build(), Competency.builder().name("Python").build()])

        candidateService.getAllInterestedInJob(42) >> [fakeDTO]

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
        List<Competency> competencies = [Competency.builder().name("Java").build(), Competency.builder().name("Python").build()]

        Candidate createdCandidate = Candidate.builder()
                .idPerson(1)
                .email(email)
                .description("desc")
                .idAddress(1)
                .idCandidate(1)
                .firstName("First")
                .lastName("Last")
                .cpf("123")
                .birthday(LocalDate.now())
                .graduation("CS")
                .build()

        Address createdCandidateAddress = Address.builder()
                .state("State")
                .postalCode("12345")
                .country("Country")
                .city("City")
                .street("Street")
                .build()

        CreateCandidateDTO createCandidateDTO = new CreateCandidateDTO("desc", "pass", email, "First", "Last", "123",
                LocalDate.now(), "CS", createdCandidateAddress, competencies)


        candidateService.createCandidate(*_) >> createdCandidate

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
        1 * candidateService.likeJob(idCandidate, idJob)
    }
}