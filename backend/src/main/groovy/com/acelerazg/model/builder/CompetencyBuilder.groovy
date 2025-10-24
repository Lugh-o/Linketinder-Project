package com.acelerazg.model.builder

import com.acelerazg.model.Competency
import groovy.transform.CompileStatic

@CompileStatic
class CompetencyBuilder {
    int id
    String name

    CompetencyBuilder id(int id) {
        this.id = id
        return this
    }

    CompetencyBuilder name(String name) {
        this.name = name
        return this
    }

    Competency build() {
        return new Competency(id, name)
    }
}
