package com.acelerazg.controller

import com.acelerazg.dao.AddressDAO
import com.acelerazg.dao.CompetencyDAO
import com.acelerazg.dao.JobDAO
import com.acelerazg.model.Competency
import com.acelerazg.model.Job
import spock.lang.Specification

class JobControllerTest extends Specification {
    AddressDAO addressDAO
    CompetencyDAO competencyDAO
    JobDAO jobDAO
    JobController controller

    def setup() {
        addressDAO = Mock()
        competencyDAO = Mock()
        jobDAO = Mock(JobDAO, constructorArgs: [addressDAO, competencyDAO])
        controller = new JobController(jobDAO)
    }

    def "HandleCreateJob"() {
        given:
        Job createdJob = new Job(1, 'name', 'description', 3, 2)
        List<Competency> competencies = [new Competency("Java"), new Competency("Python")]
        jobDAO.create(*_) >> createdJob

        when:
        Job result = controller.handleCreateJob('name', 'description', 2, 'BA', '123', 'country', 'city', 'street', competencies)

        then:
        result != null
        result.description == 'description'
        result.idCompany == 2
    }

    def "HandleGetAllByCompanyId"() {
        given:
        int idCompany = 2
        Job createdJob1 = new Job(0, 'name', 'description', 3, 2)
        Job createdJob2 = new Job(1, 'name', 'different description', 3, 2)
        Job createdJob3 = new Job(2, 'name', 'description', 3, 2)

        jobDAO.getAllByCompanyId(idCompany) >> [createdJob1, createdJob2, createdJob3]

        when:
        List<Job> result = controller.handleGetAllByCompanyId(idCompany)

        then:
        result != null
        result.size() == 3
        result[1].description == 'different description'

    }
}
