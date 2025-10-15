package com.acelerazg.controller

import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dao.CompanyDAO
import com.acelerazg.dao.JobDAO
import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.dto.CreateCompanyDTO
import com.acelerazg.model.Address
import com.acelerazg.model.Company
import com.acelerazg.model.Job
import com.acelerazg.model.LikeResult
import groovy.transform.CompileStatic

@CompileStatic
class CompanyController {
    private final CompanyDAO companyDAO
    private final CandidateDAO candidateDAO
    private final MatchEventDAO matchEventDAO
    private final JobDAO jobDAO

    CompanyController(CompanyDAO companyDAO, CandidateDAO candidateDAO, MatchEventDAO matchEventDAO, JobDAO jobDAO) {
        this.companyDAO = companyDAO
        this.candidateDAO = candidateDAO
        this.matchEventDAO = matchEventDAO
        this.jobDAO = jobDAO
    }

    List<Company> handleGetAll() {
        return companyDAO.getAll()
    }

    Company handleCreateCompany(CreateCompanyDTO createCompanyDTO) {

        Company company = new Company(createCompanyDTO.description,
                createCompanyDTO.passwd,
                createCompanyDTO.email,
                createCompanyDTO.name,
                createCompanyDTO.cnpj)

        Address address = new Address(createCompanyDTO.address.state,
                createCompanyDTO.address.postalCode,
                createCompanyDTO.address.country,
                createCompanyDTO.address.city,
                createCompanyDTO.address.street)

        return companyDAO.create(company, address)
    }

    LikeResult handleLikeCandidate(int idCompany, int idCandidate) {
        if (companyDAO.isCandidateAlreadyLiked(idCompany, idCandidate)) {
            return LikeResult.ALREADY_LIKED
        }

        companyDAO.likeCandidate(idCompany, idCandidate)
        List<Job> likedJobs = jobDAO.getAllByCompanyId(idCompany)

        boolean matchFound = likedJobs.any { job ->
            if (candidateDAO.hasLikedJob(idCandidate, job.id)) {
                matchEventDAO.create(job.id, idCandidate)
                return true
            }
            return false
        }

        return matchFound ? LikeResult.MATCH_FOUND : LikeResult.SUCCESS
    }
}