package com.acelerazg.controller

import com.acelerazg.common.Response
import com.acelerazg.dao.AddressDAO
import com.acelerazg.dao.CompetencyDAO
import com.acelerazg.dao.JobDAO
import com.acelerazg.dto.JobDTO
import com.acelerazg.model.Job
import com.acelerazg.service.JobService
import com.acelerazg.utils.JsonHandler
import groovy.transform.CompileStatic

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
@WebServlet(name = "JobController", urlPatterns = ["/api/v1/jobs/*"])
class JobController extends Controller {
    JobService jobService

    JobController() {}

    @Override
    void init() {
        AddressDAO addressDao = new AddressDAO()
        CompetencyDAO competencyDao = new CompetencyDAO()
        JobDAO jobDAO = new JobDAO(addressDao, competencyDao)
        this.jobService = new JobService(jobDAO)
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.method
        String path = req.pathInfo ?: "/"

        resp.contentType = "application/json"
        resp.characterEncoding = "UTF-8"

        try {
            // GET /api/v1/jobs/{id}/company
            if (method == "GET" && path.matches("/\\d+/company")) {
                int companyId = (path.split("/")[1] as int)
                JsonHandler.write(resp, attachPath(handleGetAllByCompanyId(companyId), req))
                return
            }

            // GET /api/v1/jobs/{id}
            if (method == "GET" && path.matches("/\\d+")) {
                int id = (path.substring(1) as int)
                JsonHandler.write(resp, attachPath(handleGetById(id), req))
                return
            }

            // POST /api/v1/jobs
            if (method == "POST" && path == "/") {
                Map body = JsonHandler.parseJsonBody(req)
                JobDTO dto = new JobDTO(body)
                JsonHandler.write(resp, attachPath(handleCreateJob(dto), req))
                return
            }

            // PUT /api/v1/jobs/{id}
            if (method == "PUT" && path.matches("/\\d+")) {
                Map body = JsonHandler.parseJsonBody(req)
                JobDTO dto = new JobDTO(body)
                int id = (path.split("/")[1] as int)
                JsonHandler.write(resp, attachPath(handleUpdateJob(id, dto), req))
                return
            }

            // DELETE /api/v1/candidates/{id}
            if (method == "DELETE" && path.matches("/\\d+")) {
                int id = (path.substring(1) as int)
                JsonHandler.write(resp, attachPath(handleDeleteJob(id), req))
                return
            }

            JsonHandler.writeError(resp, 404, "Endpoint not found")
        } catch (Exception e) {
            e.printStackTrace()
            JsonHandler.writeError(resp, 500, e.message)
        }
    }


    Response<List<JobDTO>> handleGetAllByCompanyId(int id) {
        return jobService.getAllByCompanyId(id)
    }

    Response<JobDTO> handleGetById(int id) {
        return jobService.getById(id)
    }

    Response<Job> handleCreateJob(JobDTO createJobDTO) {
        return jobService.createJob(createJobDTO)
    }

    Response<Job> handleUpdateJob(int id, JobDTO jobDTO) {
        return jobService.updateJob(id, jobDTO)
    }

    Response<Void> handleDeleteJob(int id) {
        return jobService.deleteJob(id)
    }
}