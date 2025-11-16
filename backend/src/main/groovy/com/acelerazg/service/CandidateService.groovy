package com.acelerazg.service

import com.acelerazg.common.Response
import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dto.AnonymousCandidateDTO
import com.acelerazg.model.Address
import com.acelerazg.model.Candidate
import com.acelerazg.model.Competency
import groovy.transform.CompileStatic

import javax.servlet.http.HttpServletResponse

@CompileStatic
class CandidateService {
    private final CandidateDAO candidateDAO

    CandidateService(CandidateDAO candidateDAO) {
        this.candidateDAO = candidateDAO
    }

    Response<List<Candidate>> getAllCandidates() {
        List<Candidate> candidateList = candidateDAO.getAll()
        return Response.success(HttpServletResponse.SC_OK, candidateList)
    }

    Response<Candidate> getById(int id) {
        Candidate candidate = candidateDAO.getById(id)
        if (candidate == null) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND, "Candidate not found")
        }
        return Response.success(HttpServletResponse.SC_OK, candidate)
    }

    Response<List<AnonymousCandidateDTO>> getAllInterestedInJob(int idJob) {
        List<AnonymousCandidateDTO> anonymousCandidateDTOList = candidateDAO.getAllCandidatesInterestedByJobId(idJob)
        return Response.success(HttpServletResponse.SC_OK, anonymousCandidateDTOList)
    }

    Response<Candidate> createCandidate(Candidate candidateDTO) {
        if (candidateDAO.getByEmail(candidateDTO.email)) {
            return Response.error(HttpServletResponse.SC_CONFLICT,
                    "Email already registered",
                    "The email " + candidateDTO.email + " is already used",
                    "/api/v1/candidates")
        }
        Candidate createdCandidate = candidateDAO.create(candidateDTO)
        return Response.success(HttpServletResponse.SC_CREATED, createdCandidate)
    }

    Response<Map> likeJob(int idCandidate, int idJob) {
        if (candidateDAO.isJobAlreadyLiked(idCandidate, idJob)) {
            return Response.error(HttpServletResponse.SC_CONFLICT,
                    "Like already exists",
                    "A like between candidate " + idCandidate + " and job " + idJob + " already exists.",
                    "/api/v1/candidates/" + idCandidate + "/like")
        }
        candidateDAO.likeJob(idCandidate, idJob)
        Map payload = [idCandidate: idCandidate,
                       idJob      : idJob]
        return Response.success(HttpServletResponse.SC_CREATED, payload)
    }

    Response<Candidate> updateCandidate(int id, Candidate candidate) {
        Candidate existing = candidateDAO.getById(id)
        if (!existing) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND,
                    "Candidate not found",
                    "No candidate exists with id " + id,
                    "/api/v1/candidates/" + id)
        }

        candidate.idPerson = existing.idPerson
        candidate.idAddress = existing.idAddress
        candidate.idCandidate = existing.idCandidate

        if (candidate.has("firstName")) existing.firstName = candidate.firstName
        if (candidate.has("lastName")) existing.lastName = candidate.lastName
        if (candidate.has("cpf")) existing.cpf = candidate.cpf
        if (candidate.has("graduation")) existing.graduation = candidate.graduation
        if (candidate.has("birthday")) existing.birthday = candidate.birthday

        if (candidate.has("address") && candidate.address) {
            existing.address = new Address(candidate.address.state,
                    candidate.address.postalCode,
                    candidate.address.country,
                    candidate.address.city,
                    candidate.address.street)
        }

        if (candidate.has("competencies")) {
            existing.competencies = candidate.competencies.collect { Competency c -> Competency.builder().name(c.name).build()
            }
        }

        if (candidate.has("email")) existing.email = candidate.email
        if (candidate.has("description")) existing.description = candidate.description
        if (candidate.has("passwd")) existing.passwd = candidate.passwd

        Candidate finalCandidate = candidateDAO.update(id, existing)

        return Response.success(HttpServletResponse.SC_OK, finalCandidate)
    }

    Response<Void> deleteCandidate(int id) {
        Candidate existing = candidateDAO.getById(id)
        if (!existing) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND,
                    "Candidate not found",
                    "No candidate exists with id " + id,
                    "/api/v1/candidates/" + id)
        }

        candidateDAO.delete(id)
        return Response.success(HttpServletResponse.SC_NO_CONTENT)
    }
}
