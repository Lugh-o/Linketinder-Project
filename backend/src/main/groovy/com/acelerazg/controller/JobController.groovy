package com.acelerazg.controller

import com.acelerazg.dao.JobDAO
import com.acelerazg.dto.CreateJobDTO
import com.acelerazg.model.Address
import com.acelerazg.model.Job
import groovy.transform.CompileStatic

@CompileStatic
class JobController {
    private final JobDAO jobDAO

    JobController(JobDAO jobDAO) {
        this.jobDAO = jobDAO
    }

    Job handleCreateJob(CreateJobDTO createJobDTO) {

        Job job = new Job(createJobDTO.name,
                createJobDTO.description,
                createJobDTO.idCompany)

        Address address = new Address(createJobDTO.address.state,
                createJobDTO.address.postalCode,
                createJobDTO.address.country,
                createJobDTO.address.city,
                createJobDTO.address.street)

        return jobDAO.create(job, address, createJobDTO.competencies)
    }

    List<Job> handleGetAllByCompanyId(int id) {
        return jobDAO.getAllByCompanyId(id)
    }
}