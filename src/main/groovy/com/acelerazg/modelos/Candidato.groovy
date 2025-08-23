package com.acelerazg.modelos

import com.acelerazg.traits.temCompetencias

class Candidato extends Pessoa implements temCompetencias{
    private String cpf
    private int idade

    Candidato(int id, String nome, String email, String estado, String cep, String descricao, ArrayList<Competencia> competencias, String cpf, int idade) {
        super(id, nome, email, estado, cep, descricao)
        this.competencias = competencias
        this.cpf = cpf
        this.idade = idade
    }

    String getCpf() {
        return cpf
    }

    void setCpf(String cpf) {
        this.cpf = cpf
    }

    int getIdade() {
        return idade
    }

    void setIdade(int idade) {
        this.idade = idade
    }

    @Override
    String toString() {
        return "Candidato{" +
                super.toString() +
                ", cpf='$cpf'" +
                ", idade='$idade'" +
                ", competencias='" + Arrays.toString(competencias) + "'" +
                '}'
    }
}
