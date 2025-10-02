package com.acelerazg.dto

import com.acelerazg.model.Competency
import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class AnonymousCandidateDTO {
    int id
    String graduation
    String description
    List<Competency> competencies

    AnonymousCandidateDTO(int id, String graduation, String description, List<Competency> competencies) {
        this.id = id
        this.graduation = graduation
        this.description = description
        this.competencies = competencies
    }
}