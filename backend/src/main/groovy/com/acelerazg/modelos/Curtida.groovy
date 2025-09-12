package com.acelerazg.modelos

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true)
class Curtida {
    final int id
    Candidato candidato
    Empresa empresa
    Vaga vaga

    Curtida(int id, Candidato candidato, Vaga vaga) {
        this.id = id
        this.candidato = candidato
        this.vaga = vaga
    }
}
