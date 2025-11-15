//package com.acelerazg.controller
//
//import com.acelerazg.dao.AddressDAO
//import com.acelerazg.dao.CompetencyDAO
//import com.acelerazg.dao.JobDAO
//import com.acelerazg.dto.JobDTO
//import com.acelerazg.model.Address
//import com.acelerazg.model.Competency
//import com.acelerazg.model.Job
//import com.acelerazg.service.JobService
//import spock.lang.Specification
//
//class JobControllerTest extends Specification {
//    AddressDAO addressDAO
//    CompetencyDAO competencyDAO
//    JobDAO jobDAO
//    JobService jobService
//    JobController controller
//
//    def setup() {
//        addressDAO = Mock()
//        competencyDAO = Mock()
//        jobDAO = Mock(JobDAO, constructorArgs: [addressDAO, competencyDAO])
//        jobService = Mock(JobService, constructorArgs: [jobDAO])
//        controller = new JobController(jobService)
//    }
//
//    def "HandleCreateJob"() {
//        given:
//        Job createdJob = Job.builder()
//                .id(1)
//                .name("name")
//                .description("description")
//                .idAddress(3)
//                .idCompany(2)
//                .build()
//        List<Competency> competencies = [Competency.builder().name("Java").build(), Competency.builder().name("Python").build()]
//        jobService.createJob(*_) >> createdJob
//
//        Address createdJobAddress = Address.builder()
//                .state("State")
//                .postalCode("12345")
//                .country("Country")
//                .city("City")
//                .street("Street")
//                .build()
//
//        JobDTO createJobDTO = new JobDTO('name', 'description', 2, createdJobAddress, competencies)
//
//        when:
//        Job result = controller.handleCreateJob(createJobDTO)
//
//        then:
//        result != null
//        result.description == 'description'
//        result.idCompany == 2
//    }
//
//    def "HandleGetAllByCompanyId"() {
//        given:
//        int idCompany = 2
//        Job createdJob1 = Job.builder()
//                .id(0)
//                .name("name")
//                .description("description")
//                .idAddress(3)
//                .idCompany(2)
//                .build()
//        Job createdJob2 = Job.builder()
//                .id(1)
//                .name("name")
//                .description("different description")
//                .idAddress(3)
//                .idCompany(2)
//                .build()
//        Job createdJob3 = Job.builder()
//                .id(2)
//                .name("name")
//                .description("description")
//                .idAddress(3)
//                .idCompany(2)
//                .build()
//
//        jobService.getAllByCompanyId(idCompany) >> [createdJob1, createdJob2, createdJob3]
//
//        when:
//        List<Job> result = controller.handleGetAllByCompanyId(idCompany)
//
//        then:
//        result != null
//        result.size() == 3
//        result[1].description == 'different description'
//
//    }
//}
