package com.acelerazg.controller

import com.acelerazg.common.Response
import com.acelerazg.dao.AddressDAO
import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dao.CompetencyDAO
import com.acelerazg.dao.PersonDAO
import com.acelerazg.dto.AnonymousCandidateDTO
import com.acelerazg.dto.CandidateDTO
import com.acelerazg.model.Candidate
import com.acelerazg.service.CandidateService
import com.acelerazg.utils.JsonHandler
import groovy.transform.CompileStatic

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
@WebServlet(name = "CandidateController", urlPatterns = ["/api/v1/candidates/*"])
class CandidateController extends Controller {
    CandidateService candidateService

    CandidateController() {}

    @Override
    void init() {
        AddressDAO addressDao = new AddressDAO()
        PersonDAO personDao = new PersonDAO()
        CompetencyDAO competencyDao = new CompetencyDAO()
        CandidateDAO candidateDao = new CandidateDAO(addressDao, personDao, competencyDao)

        this.candidateService = new CandidateService(candidateDao)
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.method
        String path = req.pathInfo ?: "/"

        resp.contentType = "application/json"
        resp.characterEncoding = "UTF-8"

        try {

            // GET /api/v1/candidates
            if (method == "GET" && path == "/") {
                JsonHandler.write(resp, attachPath(handleGetAll(), req))
                return
            }

            // GET /api/v1/candidates/{id}
            if (method == "GET" && path.matches("/\\d+")) {
                int id = (path.substring(1) as int)
                JsonHandler.write(resp, attachPath(handleGetById(id), req))
                return
            }

            // GET /api/v1/candidates/interested/{jobId}
            if (method == "GET" && path.matches("/interested/\\d+")) {
                int jobId = (path.split("/")[2] as int)
                JsonHandler.write(resp, attachPath(handleGetAllInterestedInJob(jobId), req))
                return
            }

            // POST /api/v1/candidates
            if (method == "POST" && path == "/") {
                Map body = JsonHandler.parseJsonBody(req)
                CandidateDTO dto = new CandidateDTO(body)
                JsonHandler.write(resp, attachPath(handleCreateCandidate(dto), req))
                return
            }

            // POST /api/v1/candidates/{id}/like
            if (method == "POST" && path.matches("/\\d+/like")) {
                int candidateId = (path.split("/")[1] as int)
                Map body = JsonHandler.parseJsonBody(req)
                int jobId = (body.get("jobId") as Integer)
                JsonHandler.write(resp, attachPath(handleLikeJob(candidateId, jobId), req))
                return
            }

            // PUT /api/v1/candidates/{id}
            if (method == "PUT" && path.matches("/\\d+")) {
                Map body = JsonHandler.parseJsonBody(req)
                CandidateDTO dto = new CandidateDTO(body)
                int id = (path.split("/")[1] as int)
                JsonHandler.write(resp, attachPath(handleUpdateCandidate(id, dto), req))
                return
            }

            // DELETE /api/v1/candidates/{id}
            if (method == "DELETE" && path.matches("/\\d+")) {
                int id = (path.substring(1) as int)
                JsonHandler.write(resp, attachPath(handleDeleteCandidate(id), req))
                return
            }

            JsonHandler.writeError(resp, 404, "Endpoint not found")

        } catch (Exception e) {
            e.printStackTrace()
            JsonHandler.writeError(resp, 500, e.message)
        }
    }


    Response<List<CandidateDTO>> handleGetAll() {
        return candidateService.getAllCandidates()
    }

    Response<Candidate> handleGetById(int id) {
        return candidateService.getById(id)
    }

    Response<List<AnonymousCandidateDTO>> handleGetAllInterestedInJob(int idJob) {
        return candidateService.getAllInterestedInJob(idJob)
    }

    Response<Candidate> handleCreateCandidate(CandidateDTO candidateDTO) {
        return candidateService.createCandidate(candidateDTO)
    }

    Response<Map> handleLikeJob(int idCandidate, int idJob) {
        return candidateService.likeJob(idCandidate, idJob)
    }

    Response<Candidate> handleUpdateCandidate(int id, CandidateDTO candidateDTO) {
        return candidateService.updateCandidate(id, candidateDTO)
    }

    Response<Void> handleDeleteCandidate(int id) {
        return candidateService.deleteCandidate(id)
    }
}