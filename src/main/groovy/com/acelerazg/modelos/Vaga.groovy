package com.acelerazg.modelos

import com.acelerazg.traits.temCompetencias

class Vaga implements temCompetencias{
    private final int id
    private String nome

    Vaga (int id, String nome, ArrayList<Competencia> competencias) {
        this.id = id
        this.nome = nome
        this.competencias = competencias
    }

    int getId() {
        return id
    }

    String getNome() {
        return nome
    }

    void setNome(String nome) {
        this.nome = nome
    }

    @Override
    String toString() {
        return "Vaga{" +
                "id='$id'" +
                ", nome='$nome'" +
                ", competencias='" + Arrays.toString(competencias) + "'" +
                '}';
    }
}
