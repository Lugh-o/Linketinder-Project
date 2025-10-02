package com.acelerazg.dto

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class MatchDTO {
    String candidateName
    String candidateGraduation

    MatchDTO(String candidateName, String candidateGraduation) {
        this.candidateName = candidateName
        this.candidateGraduation = candidateGraduation
    }
}