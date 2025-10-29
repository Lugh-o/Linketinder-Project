package com.acelerazg.service

import com.acelerazg.dao.JobDAO
import com.acelerazg.dto.CreateJobDTO
import com.acelerazg.model.Address
import com.acelerazg.model.Job
import groovy.transform.CompileStatic

@CompileStatic
class JobService {
    private final JobDAO jobDAO

    JobService(JobDAO jobDAO) {
        this.jobDAO = jobDAO
    }

    Job createJob(CreateJobDTO createJobDTO) {

        Job job = Job.builder()
                .name(createJobDTO.name)
                .description(createJobDTO.description)
                .idCompany(createJobDTO.idCompany)
                .build()

        Address address = Address.builder()
                .state(createJobDTO.address.state)
                .postalCode(createJobDTO.address.postalCode)
                .country(createJobDTO.address.country)
                .city(createJobDTO.address.city)
                .street(createJobDTO.address.street)
                .build()

        return jobDAO.create(job, address, createJobDTO.competencies)
    }

    List<Job> getAllByCompanyId(int id) {
        return jobDAO.getAllByCompanyId(id)
    }
}