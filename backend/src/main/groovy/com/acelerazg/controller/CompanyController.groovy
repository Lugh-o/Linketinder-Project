package com.acelerazg.controller

import com.acelerazg.common.CorsHandler
import com.acelerazg.common.JsonHandler
import com.acelerazg.common.Response
import com.acelerazg.dao.*
import com.acelerazg.model.Company
import com.acelerazg.service.CompanyService
import groovy.transform.CompileStatic

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
@WebServlet(name = "CompanyController", urlPatterns = ["/api/v1/companies/*"])
class CompanyController extends Controller {
    CompanyService companyService

    CompanyController() {}

    @Override
    void init() {
        AddressDAO addressDao = new AddressDAO()
        PersonDAO personDao = new PersonDAO()
        CompanyDAO companyDao = new CompanyDAO(addressDao, personDao)
        CompetencyDAO competencyDao = new CompetencyDAO()
        CandidateDAO candidateDao = new CandidateDAO(addressDao, personDao, competencyDao)
        MatchEventDAO matchEventDao = new MatchEventDAO()
        JobDAO jobDao = new JobDAO(addressDao, competencyDao)
        this.companyService = new CompanyService(companyDao, candidateDao, matchEventDao, jobDao)
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        CorsHandler.applyCorsHeaders(resp, req)

        String method = req.method
        String path = req.pathInfo ?: "/"

        resp.contentType = "application/json"
        resp.characterEncoding = "UTF-8"

        try {
            // GET /api/v1/companies
            if (method == "GET" && path == "/") {
                JsonHandler.write(resp, attachPath(handleGetAllCompanies(), req))
                return
            }

            // GET /api/v1/companies/{id}
            if (method == "GET" && path.matches("/\\d+")) {
                int id = (path.substring(1) as int)
                JsonHandler.write(resp, attachPath(handleGetById(id), req))
                return
            }

            // POST /api/v1/companies
            if (method == "POST" && path == "/") {
                Map body = JsonHandler.parseJsonBody(req)
                Company dto = new Company(body)
                JsonHandler.write(resp, attachPath(handleCreateCompany(dto), req))
                return
            }

            // POST /api/v1/companies/{id}/like
            if (method == "POST" && path.matches("/\\d+/like")) {
                int companyId = (path.split("/")[1] as int)
                Map body = JsonHandler.parseJsonBody(req)
                int candidateId = (body.get("jobId") as Integer)
                JsonHandler.write(resp, attachPath(handleLikeCandidate(companyId, candidateId), req))
                return
            }

            // PUT /api/v1/companies/{id}
            if (method == "PUT" && path.matches("/\\d+")) {
                Map body = JsonHandler.parseJsonBody(req)
                Company dto = new Company(body)
                int id = (path.split("/")[1] as int)
                JsonHandler.write(resp, attachPath(handleUpdateCompany(id, dto), req))
                return
            }

            // DELETE /api/v1/companies/{id}
            if (method == "DELETE" && path.matches("/\\d+")) {
                int id = (path.substring(1) as int)
                JsonHandler.write(resp, attachPath(handleDeleteCompany(id), req))
                return
            }

            JsonHandler.writeError(resp, 404, "Endpoint not found")

        } catch (Exception e) {
            e.printStackTrace()
            JsonHandler.writeError(resp, 500, e.message)
        }
    }

    Response<List<Company>> handleGetAllCompanies() {
        return companyService.getAllCompanies()
    }

    Response<Company> handleGetById(int id) {
        return companyService.getById(id)
    }

    Response<Company> handleCreateCompany(Company companyDTO) {
        return companyService.createCompany(companyDTO)
    }

    Response<Map> handleLikeCandidate(int idCompany, int idCandidate) {
        return companyService.likeCandidate(idCompany, idCandidate)
    }

    Response<Company> handleUpdateCompany(int id, Company companyDTO) {
        return companyService.updateCompany(id, companyDTO)
    }

    Response<Void> handleDeleteCompany(int id) {
        return companyService.deleteCompany(id)
    }
}