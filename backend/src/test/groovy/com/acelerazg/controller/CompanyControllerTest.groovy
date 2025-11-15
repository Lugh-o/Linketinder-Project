//package com.acelerazg.controller
//
//import com.acelerazg.dao.*
//import com.acelerazg.dto.CreateCompanyDTO
//import com.acelerazg.model.Address
//import com.acelerazg.model.Company
//import com.acelerazg.model.LikeResult
//import com.acelerazg.service.CompanyService
//import spock.lang.Specification
//
//class CompanyControllerTest extends Specification {
//    AddressDAO addressDAO
//    PersonDAO personDAO
//    CompetencyDAO competencyDAO
//    MatchEventDAO matchEventDAO
//    CandidateDAO candidateDAO
//    JobDAO jobDAO
//    CompanyDAO companyDAO
//    CompanyService companyService
//    CompanyController controller
//
//    def setup() {
//        addressDAO = Mock()
//        personDAO = Mock()
//        competencyDAO = Mock()
//        matchEventDAO = Mock()
//        candidateDAO = Mock(CandidateDAO, constructorArgs: [addressDAO, personDAO, competencyDAO])
//        jobDAO = Mock(JobDAO, constructorArgs: [addressDAO, competencyDAO])
//        companyDAO = Mock(CompanyDAO, constructorArgs: [addressDAO, personDAO])
//        companyService = Mock(CompanyService, constructorArgs: [companyDAO, candidateDAO, matchEventDAO, jobDAO])
//        controller = new CompanyController(companyService)
//    }
//
//    def "HandleGetAll"() {
//        given:
//        Company fake1 = Company.builder()
//                .idPerson(1)
//                .email("email")
//                .description("description")
//                .idAddress(1)
//                .idCompany(2)
//                .name("name")
//                .cnpj("cnpj")
//                .build()
//        Company fake2 = Company.builder()
//                .idPerson(1)
//                .email("email")
//                .description("description")
//                .idAddress(1)
//                .idCompany(2)
//                .name("name")
//                .cnpj("cnpj")
//                .build()
//
//        companyService.getAllCompanies() >> [fake1, fake2]
//
//        when:
//        List<Company> companyList = controller.handleGetAllCompanies()
//
//        then:
//        companyList.size() == 2
//        companyList[0].email == "email"
//    }
//
//    def "HandleCreateCompany"() {
//        given:
//        Company createdCompany = Company.builder()
//                .email("email")
//                .description("description")
//                .name("name")
//                .cnpj("cnpj")
//                .build()
//
//        companyService.createCompany(*_) >> createdCompany
//
//        Address createdCompanyAddress = Address.builder()
//                .state("State")
//                .postalCode("12345")
//                .country("Country")
//                .city("City")
//                .street("Street")
//                .build()
//
//        CreateCompanyDTO createCompanyDTO = new CreateCompanyDTO('description', 'password', 'email', 'name', 'cnpj', createdCompanyAddress)
//
//        when:
//        Company result = controller.handleCreateCompany(createCompanyDTO)
//
//        then:
//        result != null
//        result.email == 'email'
//        result.cnpj == 'cnpj'
//    }
//
//    def "HandleLikeCandidate"() {
//        int idCompany = 2
//        int idCandidate = 5
//        companyService.likeCandidate(idCompany, idCandidate) >> LikeResult.SUCCESS
//
//        when:
//        LikeResult result = controller.handleLikeCandidate(idCompany, idCandidate)
//
//        then:
//        result == LikeResult.SUCCESS
//    }
//}