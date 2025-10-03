package com.acelerazg.controller

import com.acelerazg.dao.JobDAO
import com.acelerazg.model.Address
import com.acelerazg.model.Competency
import com.acelerazg.model.Job
import groovy.transform.CompileStatic

@CompileStatic
class JobController {
    private final JobDAO jobDao

    JobController(JobDAO jobDao) {
        this.jobDao = jobDao
    }

    Job handleCreateJob(String name, String description, int idCompany, String state, String postalCode, String country, String city, String street, List<Competency> competencies) {

        Job job = new Job(
                name,
                description,
                idCompany
        )

        Address address = new Address(
                state,
                postalCode,
                country,
                city,
                street
        )

        return jobDao.create(job, address, competencies)
    }

    List<Job> handleGetAllByCompanyId(int id) {
        return jobDao.getAllByCompanyId(id)
    }
}