package com.acelerazg.traits

import com.acelerazg.modelos.Competencia

/**
 * Trait que fornece a capacidade de gerenciar competências.
 *
 * <p>Qualquer classe que implemente este trait poderá armazenar e adicionar competências.</p>
 */
trait temCompetencias {
    ArrayList<Competencia> competencias

    void addCompetencia(Competencia competencia) {
        competencias.add(competencia)
    }
}