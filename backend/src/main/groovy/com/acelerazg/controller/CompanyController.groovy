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
    static List<Company> handleGetAll() {
        return CompanyDAO.getAll()
    }

    static Company createCompany(String description, String passwd, String email, String name, String cnpj, String state, String postalCode, String country, String city, String street) {

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

        return CompanyDAO.create(company, address)
    }

    static void handleLikeCandidate(int idCompany, int idCandidate) {
        CompanyDAO.likeCandidate(idCompany, idCandidate)

        List<Job> likedJobs = JobDAO.getAllByCompanyId(idCompany)
        likedJobs.each { job ->
            if (CandidateDAO.hasLikedJob(idCandidate, job.id)) {
                MatchEventDAO.create(job.id, idCandidate)
            }
        }
    }
}
