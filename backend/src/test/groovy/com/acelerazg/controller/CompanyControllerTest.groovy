package com.acelerazg.controller

import com.acelerazg.dao.*
import com.acelerazg.dto.CreateCompanyDTO
import com.acelerazg.model.Address
import com.acelerazg.model.Company
import spock.lang.Specification

class CompanyControllerTest extends Specification {
    AddressDAO addressDAO
    PersonDAO personDAO
    CompetencyDAO competencyDAO
    MatchEventDAO matchEventDAO
    CandidateDAO candidateDAO
    JobDAO jobDAO
    CompanyDAO companyDAO
    CompanyController controller

    def setup() {
        addressDAO = Mock()
        personDAO = Mock()
        competencyDAO = Mock()
        matchEventDAO = Mock()
        candidateDAO = Mock(CandidateDAO, constructorArgs: [addressDAO, personDAO, competencyDAO])
        jobDAO = Mock(JobDAO, constructorArgs: [addressDAO, competencyDAO])
        companyDAO = Mock(CompanyDAO, constructorArgs: [addressDAO, personDAO])
        controller = new CompanyController(companyDAO, candidateDAO, matchEventDAO, jobDAO)
    }

    def "HandleGetAll"() {
        given:
        Company fake1 = Company.builder()
                .idPerson(1)
                .email("email")
                .description("description")
                .idAddress(1)
                .idCompany(2)
                .name("name")
                .cnpj("cnpj")
                .build()
        Company fake2 = Company.builder()
                .idPerson(1)
                .email("email")
                .description("description")
                .idAddress(1)
                .idCompany(2)
                .name("name")
                .cnpj("cnpj")
                .build()

        companyDAO.getAll() >> [fake1, fake2]

        when:
        List<Company> companyList = controller.handleGetAll()

        then:
        companyList.size() == 2
        companyList[0].email == "email"
    }

    def "HandleCreateCompany"() {
        given:
        Company createdCompany = Company.builder()
                .email("email")
                .description("description")
                .name("name")
                .cnpj("cnpj")
                .build()

        companyDAO.create(*_) >> createdCompany

        Address createdCompanyAddress = Address.builder()
                .state("State")
                .postalCode("12345")
                .country("Country")
                .city("City")
                .street("Street")
                .build()

        CreateCompanyDTO createCompanyDTO = new CreateCompanyDTO('description', 'password', 'email', 'name', 'cnpj', createdCompanyAddress)

        when:
        Company result = controller.handleCreateCompany(createCompanyDTO)

        then:
        result != null
        result.email == 'email'
        result.cnpj == 'cnpj'
    }

    def "HandleLikeCandidateAlreadyLiked"() {
        given:
        int idCompany = 2
        int idCandidate = 5

        companyDAO.isCandidateAlreadyLiked(idCompany, idCandidate) >> true

        when:
        controller.handleLikeCandidate(idCompany, idCandidate)

        then:
        0 * companyDAO.likeCandidate(_, _)
    }

    def "HandleLikeCandidate"() {
        int idCompany = 2
        int idCandidate = 5

        companyDAO.isCandidateAlreadyLiked(idCompany, idCandidate) >> false
        jobDAO.getAllByCompanyId(idCompany) >> []

        when:
        controller.handleLikeCandidate(idCompany, idCandidate)

        then:
        1 * companyDAO.likeCandidate(_, _)
    }
}