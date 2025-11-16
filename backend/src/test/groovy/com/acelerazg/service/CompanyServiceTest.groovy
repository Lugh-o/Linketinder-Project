package com.acelerazg.service

import com.acelerazg.common.Response
import com.acelerazg.dao.*
import com.acelerazg.model.Address
import com.acelerazg.model.Company
import spock.lang.Specification

class CompanyServiceTest extends Specification {
    AddressDAO addressDAO
    PersonDAO personDAO
    CompetencyDAO competencyDAO
    MatchEventDAO matchEventDAO
    CandidateDAO candidateDAO
    JobDAO jobDAO
    CompanyDAO companyDAO
    CompanyService companyService

    def setup() {
        addressDAO = Mock()
        personDAO = Mock()
        competencyDAO = Mock()
        matchEventDAO = Mock()
        candidateDAO = Mock(CandidateDAO, constructorArgs: [addressDAO, personDAO, competencyDAO])
        jobDAO = Mock(JobDAO, constructorArgs: [addressDAO, competencyDAO])
        companyDAO = Mock(CompanyDAO, constructorArgs: [addressDAO, personDAO])
        companyService = new CompanyService(companyDAO, candidateDAO, matchEventDAO, jobDAO)
    }

    def "GetAll"() {
        Address address = Address.builder()
                .state("state")
                .postalCode("postal_code")
                .country("country")
                .street("street")
                .city("city")
                .build()
        given:
        Company fake1 = new Company("description",
                "email",
                "fake1",
                "cnpj",
                1,
                address,
                1,
                1)
        Company fake2 = new Company("description",
                "email",
                "fake2",
                "cnpj",
                1,
                address,
                1,
                1)
        companyDAO.getAll() >> [fake1, fake2]

        when:
       Response<List<Company>> response = companyService.getAllCompanies()

        then:
        response.data.size() == 2
        response.data[0].email == "email"
    }

    def "CreateCompany"() {
        given:
        Address address = Address.builder()
                .state("state")
                .postalCode("postal_code")
                .country("country")
                .street("street")
                .city("city")
                .build()

        Company company = new Company("description",
                "email",
                "fake",
                "cnpj",
                1,
                address,
                1,
                null)

        Company created = new Company("description",
                "email",
                "fake",
                "cnpj",
                1,
                address,
                1,
                1)

        companyDAO.create(company) >> created

        when:
        Response<Company> result = companyService.createCompany(company)

        then:
        result != null
        result.data.email == company.email
        result.data.cnpj == company.cnpj
        result.data.idCompany != company.idCompany
    }
}
