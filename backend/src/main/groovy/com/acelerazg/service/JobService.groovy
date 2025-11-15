package com.acelerazg.service

import com.acelerazg.common.Response
import com.acelerazg.dao.JobDAO
import com.acelerazg.dto.JobDTO
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

    Response<List<JobDTO>> getAllByCompanyId(int id) {
        List<JobDTO> jobList = jobDAO.getAllByCompanyId(id)
        return Response.success(HttpServletResponse.SC_OK, jobList)
    }

    Response<JobDTO> getById(int id) {
        JobDTO job = jobDAO.getById(id)
        if (job == null) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND, "Job not found")
        }
        return Response.success(HttpServletResponse.SC_OK, job)
    }

    Response<Job> createJob(JobDTO jobDTO) {
        Job job = Job.builder()
                .name(jobDTO.name)
                .description(jobDTO.description)
                .idCompany(jobDTO.idCompany)
                .build()

        Address address = Address.builder()
                .state(jobDTO.address.state)
                .postalCode(jobDTO.address.postalCode)
                .country(jobDTO.address.country)
                .city(jobDTO.address.city)
                .street(jobDTO.address.street)
                .build()

        job = jobDAO.create(job, address, jobDTO.competencies)

        return Response.success(HttpServletResponse.SC_CREATED, job)
    }

    Response<Job> updateJob(int id, JobDTO jobDTO) {
        JobDTO existingDTO = jobDAO.getById(id)
        if (!existingDTO) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND,
                    "Job not found",
                    "No job exists with id " + id,
                    "/api/v1/jobs/" + id)
        }
        Job existing = existingDTO.toModel()

        jobDTO.idCompany = existing.idCompany
        jobDTO.idAddress = existing.idAddress
        jobDTO.idJob = existing.id

        if (jobDTO.has("name")) existing.name = jobDTO.name
        if (jobDTO.has("description")) existing.description = jobDTO.description

        Address updatedAddress = null
        if (jobDTO.has("address") && jobDTO.address) {
            updatedAddress = new Address(jobDTO.address.state,
                    jobDTO.address.postalCode,
                    jobDTO.address.country,
                    jobDTO.address.city,
                    jobDTO.address.street)
        }

        List<Competency> updatedCompetencies
        if (jobDTO.has("competencies")) {
            updatedCompetencies = jobDTO.competencies.collect { Competency c -> Competency.builder().name(c.name).build()
            }
        } else {
            updatedCompetencies = null
        }

        Job finalJob = jobDAO.update(id, existing, updatedAddress, updatedCompetencies)
        return Response.success(HttpServletResponse.SC_OK, finalJob)

    }

    Response<Void> deleteJob(int id) {
        JobDTO existingDTO = jobDAO.getById(id)
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