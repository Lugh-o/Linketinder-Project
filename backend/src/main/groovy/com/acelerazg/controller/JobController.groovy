package com.acelerazg.controller

import com.acelerazg.dto.CreateJobDTO
import com.acelerazg.model.Job
import com.acelerazg.service.JobService
import groovy.transform.CompileStatic

@CompileStatic
class JobController {
    private final JobService jobService

    JobController(JobService jobService) {
        this.jobService = jobService
    }

    Job handleCreateJob(CreateJobDTO createJobDTO) {
        return jobService.createJob(createJobDTO)
    }

    List<Job> handleGetAllByCompanyId(int id) {
        return jobService.getAllByCompanyId(id)
    }
}