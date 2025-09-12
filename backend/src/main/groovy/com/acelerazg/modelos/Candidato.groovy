package com.acelerazg.modelos

import com.acelerazg.traits.temCompetencias
import groovy.transform.ToString

@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class Candidato extends Pessoa implements temCompetencias {
    String cpf
    int idade

    Candidato(int id, String nome, String email, String estado, String cep, String descricao, ArrayList<Competencia> competencias, String cpf, int idade) {
        super(id, nome, email, estado, cep, descricao)
        this.competencias = competencias
        this.cpf = cpf
        this.idade = idade
    }
}
