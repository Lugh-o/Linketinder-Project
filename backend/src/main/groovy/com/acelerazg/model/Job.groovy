package com.acelerazg.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class Job {
    String name
    String description
    Address address
    List<Competency> competencies
    Integer idCompany
    Integer idAddress
    Integer idJob
    Map originalMap

    Job(String name, String description, Address address, List<Competency> competencies, Integer idCompany, Integer idAddress, Integer idJob) {
        this.name = name
        this.description = description
        this.address = address
        this.competencies = competencies ?: []
        this.idCompany = idCompany
        this.idAddress = idAddress
        this.idJob = idJob
    }

    Job(Map map) {
        this.originalMap = map
        this.name = map.name
        this.description = map.description
        this.idCompany = map.idCompany as Integer
        this.idJob = map.idJob as Integer

        def addressMap = map.address
        if (addressMap instanceof Map) {
            this.address = new Address(addressMap.get("state") as String,
                    addressMap.get("postalCode") as String,
                    addressMap.get("country") as String,
                    addressMap.get("city") as String,
                    addressMap.get("street") as String)
        } else {
            this.address = null
        }

        this.competencies = map.competencies instanceof List ? map.competencies.collect { Object name ->
            Competency.builder()
                    .name(name as String)
                    .build()
        } : []
    }

    boolean has(String key) {
        return originalMap.containsKey(key)
    }
}
