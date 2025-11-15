package com.acelerazg.service

import com.acelerazg.common.Response
import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dao.CompanyDAO
import com.acelerazg.dao.JobDAO
import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.dto.CompanyDTO
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

    Response<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companyList = companyDAO.getAll()
        return Response.success(HttpServletResponse.SC_OK, companyList)
    }

    Response<CompanyDTO> getById(int id) {
        CompanyDTO company = companyDAO.getById(id)
        if (company == null) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND, "Company not found");
        }
        return Response.success(HttpServletResponse.SC_OK, company)
    }

    Response<Company> createCompany(CompanyDTO CompanyDTO) {
        if (companyDAO.getByEmail(CompanyDTO.email)) {
            return Response.error(HttpServletResponse.SC_CONFLICT,
                    "Email already registered",
                    "The email " + CompanyDTO.email + " is already used",
                    "/api/v1/candidates")
        }

        Company company = Company.builder()
                .description(CompanyDTO.description)
                .passwd(CompanyDTO.passwd)
                .email(CompanyDTO.email)
                .name(CompanyDTO.name)
                .cnpj(CompanyDTO.cnpj)
                .build()

        Address address = Address.builder()
                .state(CompanyDTO.address.state)
                .postalCode(CompanyDTO.address.postalCode)
                .country(CompanyDTO.address.country)
                .city(CompanyDTO.address.city)
                .street(CompanyDTO.address.street)
                .build()

        company = companyDAO.create(company, address)

        return Response.success(HttpServletResponse.SC_CREATED, company)
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

        likedJobs.any { Job job ->
            if (candidateDAO.hasLikedJob(idCandidate, job.id)) {
                matchEventDAO.create(job.id, idCandidate)
                payload.match = true
                return Response.success(HttpServletResponse.SC_CREATED, payload)
            }
        }
        return Response.success(HttpServletResponse.SC_CREATED, payload)
    }

    Response<Company> updateCompany(int id, CompanyDTO companyDTO) {
        Company existing = companyDAO.getById(id).toModel()
        if (!existing) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND,
                    "Company not found",
                    "No company exists with id " + id,
                    "/api/v1/companies/" + id)
        }

        companyDTO.idPerson = existing.idPerson
        companyDTO.idAddress = existing.idAddress
        companyDTO.idCompany = existing.idCompany

        if (companyDTO.has("name")) existing.name = companyDTO.name
        if (companyDTO.has("cnpj")) existing.cnpj = companyDTO.cnpj

        Address updatedAddress = null
        if (companyDTO.has("address") && companyDTO.address) {
            updatedAddress = new Address(companyDTO.address.state,
                    companyDTO.address.postalCode,
                    companyDTO.address.country,
                    companyDTO.address.city,
                    companyDTO.address.street)
        }

        if (companyDTO.has("email")) existing.email = companyDTO.email
        if (companyDTO.has("description")) existing.description = companyDTO.description
        if (companyDTO.has("passwd")) existing.passwd = companyDTO.passwd

        Company finalCompany = companyDAO.update(id, existing, updatedAddress)
        return Response.success(HttpServletResponse.SC_OK, finalCompany)
    }

    Response<Void> deleteCompany(int id) {
        Company existing = companyDAO.getById(id).toModel()
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