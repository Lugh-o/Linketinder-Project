package com.acelerazg.service

import com.acelerazg.common.Response
import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dao.CompanyDAO
import com.acelerazg.dao.JobDAO
import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.model.Address
import com.acelerazg.model.Company
import com.acelerazg.model.Job
import groovy.transform.CompileStatic

import javax.servlet.http.HttpServletResponse

@CompileStatic
class CompanyService {
    private final CompanyDAO companyDAO
    private final CandidateDAO candidateDAO
    private final MatchEventDAO matchEventDAO
    private final JobDAO jobDAO

    CompanyService(CompanyDAO companyDAO, CandidateDAO candidateDAO, MatchEventDAO matchEventDAO, JobDAO jobDAO) {
        this.companyDAO = companyDAO
        this.candidateDAO = candidateDAO
        this.matchEventDAO = matchEventDAO
        this.jobDAO = jobDAO
    }

    Response<List<Company>> getAllCompanies() {
        List<Company> companyList = companyDAO.getAll()
        return Response.success(HttpServletResponse.SC_OK, companyList)
    }

    Response<Company> getById(int id) {
        Company company = companyDAO.getById(id)
        if (company == null) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND, "Company not found")
        }
        return Response.success(HttpServletResponse.SC_OK, company)
    }

    Response<Company> createCompany(Company company) {
        if (companyDAO.getByEmail(company.email)) {
            return Response.error(HttpServletResponse.SC_CONFLICT,
                    "Email already registered",
                    "The email " + company.email + " is already used",
                    "/api/v1/candidates")
        }

        Company createdCompany = companyDAO.create(company)

        return Response.success(HttpServletResponse.SC_CREATED, createdCompany)
    }

    Response<Map> likeCandidate(int idCompany, int idCandidate) {
        if (companyDAO.isCandidateAlreadyLiked(idCompany, idCandidate)) {
            return Response.error(HttpServletResponse.SC_CONFLICT,
                    "Like already exists",
                    "A like between company " + idCompany + " and candidate " + idCandidate + " already exists.",
                    "/api/v1/companies/" + idCompany + "/like")
        }

        companyDAO.likeCandidate(idCompany, idCandidate)

        Map payload = [idCompany  : idCompany,
                       idCandidate: idCandidate,
                       match      : false]

        List<Job> likedJobs = jobDAO.getAllByCompanyId(idCompany)

        likedJobs.any { Job jobDTO ->
            if (candidateDAO.hasLikedJob(idCandidate, jobDTO.idJob)) {
                matchEventDAO.create(jobDTO.idJob, idCandidate)
                payload.match = true
                return Response.success(HttpServletResponse.SC_CREATED, payload)
            }
        }
        return Response.success(HttpServletResponse.SC_CREATED, payload)
    }

    Response<Company> updateCompany(int id, Company company) {
        Company existingDTO = companyDAO.getById(id)
        if (!existingDTO) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND,
                    "Company not found",
                    "No company exists with id " + id,
                    "/api/v1/companies/" + id)
        }

        company.idPerson = existingDTO.idPerson
        company.idAddress = existingDTO.idAddress
        company.idCompany = existingDTO.idCompany

        if (company.has("name")) existingDTO.name = company.name
        if (company.has("cnpj")) existingDTO.cnpj = company.cnpj

        if (company.has("address") && company.address) {
            existingDTO.address = new Address(company.address.state,
                    company.address.postalCode,
                    company.address.country,
                    company.address.city,
                    company.address.street)
        }

        if (company.has("email")) existingDTO.email = company.email
        if (company.has("description")) existingDTO.description = company.description
        if (company.has("passwd")) existingDTO.passwd = company.passwd

        Company finalCompany = companyDAO.update(id, existingDTO)
        return Response.success(HttpServletResponse.SC_OK, finalCompany)
    }

    Response<Void> deleteCompany(int id) {
        Company existing = companyDAO.getById(id)
        if (!existing) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND,
                    "Company not found",
                    "No company exists with id " + id,
                    "/api/v1/companies/" + id)
        }

        companyDAO.delete(id)
        return Response.success(HttpServletResponse.SC_NO_CONTENT)
    }
}