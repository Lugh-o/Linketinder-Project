package com.acelerazg.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class Competency {
    int id
    String name

    Competency(String name) {
        this.name = name
    }

    Competency(String name, int id) {
        this.name = name
        this.id = id
    }
}