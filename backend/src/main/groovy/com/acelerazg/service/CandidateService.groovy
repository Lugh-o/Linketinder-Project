package com.acelerazg.service

import com.acelerazg.common.Response
import com.acelerazg.dao.CandidateDAO
import com.acelerazg.dto.AnonymousCandidateDTO
import com.acelerazg.dto.CandidateDTO
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

    Response<List<CandidateDTO>> getAllCandidates() {
        List<CandidateDTO> candidateList = candidateDAO.getAll()
        return Response.success(HttpServletResponse.SC_OK, candidateList)
    }

    Response<Candidate> getById(int id) {
        Candidate candidate = candidateDAO.getById(id)
        if (candidate == null) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND, "Candidate not found");
        }
        return Response.success(HttpServletResponse.SC_OK, candidate)
    }

    Response<List<AnonymousCandidateDTO>> getAllInterestedInJob(int idJob) {
        List<AnonymousCandidateDTO> anonymousCandidateDTOList = candidateDAO.getAllCandidatesInterestedByJobId(idJob)
        return Response.success(HttpServletResponse.SC_OK, anonymousCandidateDTOList)
    }

    Response<Candidate> createCandidate(CandidateDTO candidateDTO) {

        if (candidateDAO.getByEmail(candidateDTO.email)) {
            return Response.error(HttpServletResponse.SC_CONFLICT,
                    "Email already registered",
                    "The email " + candidateDTO.email + " is already used",
                    "/api/v1/candidates")
        }

        Candidate candidate = Candidate.builder()
                .description(candidateDTO.description)
                .passwd(candidateDTO.passwd)
                .email(candidateDTO.email)
                .firstName(candidateDTO.firstName)
                .lastName(candidateDTO.lastName)
                .cpf(candidateDTO.cpf)
                .birthday(candidateDTO.birthday)
                .graduation(candidateDTO.graduation)
                .build()

        Address address = Address.builder()
                .state(candidateDTO.address.state)
                .postalCode(candidateDTO.address.postalCode)
                .country(candidateDTO.address.country)
                .city(candidateDTO.address.city)
                .street(candidateDTO.address.street)
                .build()

        candidate = candidateDAO.create(candidate, address, candidateDTO.competencies)

        return Response.success(HttpServletResponse.SC_CREATED, candidate)
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

    Response<Candidate> updateCandidate(int id, CandidateDTO candidateDTO) {
        Candidate existing = candidateDAO.getById(id)
        if (!existing) {
            return Response.error(HttpServletResponse.SC_NOT_FOUND,
                    "Candidate not found",
                    "No candidate exists with id " + id,
                    "/api/v1/candidates/" + id)
        }

        candidateDTO.idPerson = existing.idPerson
        candidateDTO.idAddress = existing.idAddress
        candidateDTO.idCandidate = existing.idCandidate

        if (candidateDTO.has("firstName")) existing.firstName = candidateDTO.firstName
        if (candidateDTO.has("lastName")) existing.lastName = candidateDTO.lastName
        if (candidateDTO.has("cpf")) existing.cpf = candidateDTO.cpf
        if (candidateDTO.has("graduation")) existing.graduation = candidateDTO.graduation
        if (candidateDTO.has("birthday")) existing.birthday = candidateDTO.birthday

        Address updatedAddress = null
        if (candidateDTO.has("address") && candidateDTO.address) {
            updatedAddress = new Address(candidateDTO.address.state,
                    candidateDTO.address.postalCode,
                    candidateDTO.address.country,
                    candidateDTO.address.city,
                    candidateDTO.address.street)
        }

        List<Competency> updatedCompetencies
        if (candidateDTO.has("competencies")) {
            updatedCompetencies = candidateDTO.competencies.collect { Competency c -> Competency.builder().name(c.name).build()
            }
        } else {
            updatedCompetencies = null
        }

        if (candidateDTO.has("email")) existing.email = candidateDTO.email
        if (candidateDTO.has("description")) existing.description = candidateDTO.description
        if (candidateDTO.has("passwd")) existing.passwd = candidateDTO.passwd

        Candidate finalCandidate = candidateDAO.update(id, existing, updatedAddress, updatedCompetencies)

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
