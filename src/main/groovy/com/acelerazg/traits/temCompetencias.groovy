package com.acelerazg.traits

import com.acelerazg.modelos.Competencia

trait temCompetencias {
    ArrayList<Competencia> competencias

    void addCompetencia(Competencia competencia) {
        competencias.add(competencia)
    }
}