package com.acelerazg.controller

import com.acelerazg.dto.CreateCompanyDTO
import com.acelerazg.model.Company
import com.acelerazg.model.LikeResult
import com.acelerazg.service.CompanyService
import groovy.transform.CompileStatic

@CompileStatic
class CompanyController {
    private final CompanyService companyService

    CompanyController(CompanyService companyService) {
        this.companyService = companyService
    }

    List<Company> handleGetAllCompanies() {
        return companyService.getAllCompanies()
    }

    Company handleCreateCompany(CreateCompanyDTO createCompanyDTO) {
        return companyService.createCompany(createCompanyDTO)
    }

    LikeResult handleLikeCandidate(int idCompany, int idCandidate) {
        return companyService.likeCandidate(idCompany, idCandidate)
    }
}