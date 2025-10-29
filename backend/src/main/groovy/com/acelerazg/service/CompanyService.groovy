package com.acelerazg.service

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

    List<Company> getAllCompanies() {
        return companyDAO.getAll()
    }

    Company createCompany(CreateCompanyDTO createCompanyDTO) {

        Company company = Company.builder()
                .description(createCompanyDTO.description)
                .passwd(createCompanyDTO.passwd)
                .email(createCompanyDTO.email)
                .name(createCompanyDTO.name)
                .cnpj(createCompanyDTO.cnpj)
                .build()

        Address address = Address.builder()
                .state(createCompanyDTO.address.state)
                .postalCode(createCompanyDTO.address.postalCode)
                .country(createCompanyDTO.address.country)
                .city(createCompanyDTO.address.city)
                .street(createCompanyDTO.address.street)
                .build()

        return companyDAO.create(company, address)
    }

    LikeResult likeCandidate(int idCompany, int idCandidate) {
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