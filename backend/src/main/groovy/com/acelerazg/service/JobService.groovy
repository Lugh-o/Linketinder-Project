package com.acelerazg.service

import com.acelerazg.common.Response
import com.acelerazg.dao.JobDAO
import com.acelerazg.model.Address
import com.acelerazg.model.Competency
import com.acelerazg.model.Job
import groovy.transform.CompileStatic

import javax.servlet.http.HttpServletResponse

@CompileStatic
class JobService {
    private final JobDAO jobDAO

    JobService(JobDAO jobDAO) {
        this.jobDAO = jobDAO
    }

    Response<List<Job>> getAllByCompanyId(int id) {
        List<Job> jobList = jobDAO.getAllByCompanyId(id)
        return Response.success(HttpServletResponse.SC_OK, jobList)
    }

    Response<Job> getById(int id) {
        Job job = jobDAO.getById(id)
        if (job == null) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND, "Job not found")
        }
        return Response.success(HttpServletResponse.SC_OK, job)
    }

    Response<Job> createJob(Job job) {
        Job createdJob = jobDAO.create(job)
        return Response.success(HttpServletResponse.SC_CREATED, createdJob)
    }

    Response<Job> updateJob(int id, Job job) {
        Job existingDTO = jobDAO.getById(id)
        if (!existingDTO) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND,
                    "Job not found",
                    "No job exists with id " + id,
                    "/api/v1/jobs/" + id)
        }
        Job existing = existingDTO

        job.idCompany = existing.idCompany
        job.idAddress = existing.idAddress
        job.idJob = existing.idJob

        if (job.has("name")) existing.name = job.name
        if (job.has("description")) existing.description = job.description

        if (job.has("address") && job.address) {
            job.address = new Address(job.address.state,
                    job.address.postalCode,
                    job.address.country,
                    job.address.city,
                    job.address.street)
        }

        if (job.has("competencies")) {
            job.competencies = job.competencies.collect { Competency c -> Competency.builder().name(c.name).build()
            }
        }

        Job finalJob = jobDAO.update(id, job)
        return Response.success(HttpServletResponse.SC_OK, finalJob)

    }

    Response<Void> deleteJob(int id) {
        Job existingDTO = jobDAO.getById(id)
        if (!existingDTO) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND,
                    "Job not found",
                    "No job exists with id " + id,
                    "/api/v1/jobs/" + id)
        }

        jobDAO.delete(id)
        return Response.success(HttpServletResponse.SC_NO_CONTENT)
    }
}