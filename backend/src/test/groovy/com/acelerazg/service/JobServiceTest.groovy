package com.acelerazg.service

import com.acelerazg.common.Response
import com.acelerazg.dao.AddressDAO
import com.acelerazg.dao.CompetencyDAO
import com.acelerazg.dao.JobDAO
import com.acelerazg.model.Address
import com.acelerazg.model.Competency
import com.acelerazg.model.Job
import spock.lang.Specification

class JobServiceTest extends Specification {
    AddressDAO addressDAO
    CompetencyDAO competencyDAO
    JobDAO jobDAO
    JobService jobService

    def setup() {
        addressDAO = Mock()
        competencyDAO = Mock()
        jobDAO = Mock(JobDAO, constructorArgs: [addressDAO, competencyDAO])
        jobService = new JobService(jobDAO)
    }

    def "HandleCreateJob"() {
        given:
        Address address = Address.builder()
                .state("state")
                .postalCode("postal_code")
                .country("country")
                .street("street")
                .city("city")
                .build()
        List<Competency> competencies = [Competency.builder().name("Java").build(), Competency.builder().name("Python").build()]

        Job job = new Job(
                "name",
                "description",
                address,
                competencies,
                1,
                1,
                null
        )
        Job created = new Job(
                "name",
                "description",
                address,
                competencies,
                1,
                1,
                1
        )
        jobDAO.create(job) >> created

        when:
        Response<Job> result = jobService.createJob(job)

        then:
        result != null
        result.data.description == job.description
        result.data.idCompany == job.idCompany
        result.data.idJob != job.idJob
    }

    def "HandleGetAllByCompanyId"() {
        given:
        int idCompany = 2
        Address address = Address.builder()
                .state("state")
                .postalCode("postal_code")
                .country("country")
                .street("street")
                .city("city")
                .build()
        List<Competency> competencies = [Competency.builder().name("Java").build(), Competency.builder().name("Python").build()]

        Job job1 = new Job(
                "name",
                "description",
                address,
                competencies,
                idCompany,
                1,
                1
        )
        Job job2 = new Job(
                "name",
                "different description",
                address,
                competencies,
                idCompany,
                1,
                2
        )
        Job job3 = new Job(
                "name",
                "description",
                address,
                competencies,
                idCompany,
                1,
                3
        )
        jobDAO.getAllByCompanyId(idCompany) >> [job1, job2, job3]

        when:
        Response<List<Job>> response = jobService.getAllByCompanyId(idCompany)

        then:
        response.data != null
        response.data.size() == 3
        response.data[1].description == 'different description'
    }
}
