package com.acelerazg.modelos

import com.acelerazg.traits.temCompetencias
import groovy.transform.ToString

@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class Vaga implements temCompetencias {
    final int id
    String nome

    Vaga (int id, String nome, ArrayList<Competencia> competencias) {
        this.id = id
        this.nome = nome
        this.competencias = competencias
    }
}
