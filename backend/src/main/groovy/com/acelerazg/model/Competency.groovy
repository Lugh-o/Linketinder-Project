package com.acelerazg.model

import com.acelerazg.model.builder.CompetencyBuilder
import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class Competency {
    int id
    String name

    Competency(int id, String name) {
        this.id = id
        this.name = name
    }

    static CompetencyBuilder builder() {
        return new CompetencyBuilder()
    }
}