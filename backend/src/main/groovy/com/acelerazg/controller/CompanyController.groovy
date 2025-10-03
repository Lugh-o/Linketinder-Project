package com.acelerazg.controller

import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dao.CompanyDAO
import com.acelerazg.dao.JobDAO
import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.model.Address
import com.acelerazg.model.Company
import com.acelerazg.model.Job
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

    Company handleCreateCompany(String description, String passwd, String email, String name, String cnpj, String state, String postalCode, String country, String city, String street) {

        Company company = new Company(
                description,
                passwd,
                email,
                name,
                cnpj
        )

        Address address = new Address(
                state,
                postalCode,
                country,
                city,
                street
        )

        return companyDAO.create(company, address)
    }

    boolean handleLikeCandidate(int idCompany, int idCandidate) {
        if (companyDAO.isCandidateAlreadyLiked(idCompany, idCandidate)) return false

        companyDAO.likeCandidate(idCompany, idCandidate)
        List<Job> likedJobs = jobDAO.getAllByCompanyId(idCompany)
        likedJobs.each { job ->
            if (candidateDAO.hasLikedJob(idCandidate, job.id)) {
                matchEventDAO.create(job.id, idCandidate)
            }
        }
        return true
    }
}
