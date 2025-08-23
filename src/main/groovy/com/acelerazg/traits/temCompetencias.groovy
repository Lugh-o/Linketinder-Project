package com.acelerazg.traits

import com.acelerazg.modelos.Competencia

trait temCompetencias {
    private ArrayList<Competencia> competencias

    ArrayList<Competencia> getCompetencias() {
        return competencias
    }

    void setCompetencias(ArrayList<Competencia> competencias) {
        this.competencias = competencias
    }

    void addCompetencia(Competencia competencia) {
        competencias.add(competencia)
    }
}