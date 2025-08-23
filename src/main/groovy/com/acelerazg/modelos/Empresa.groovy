package com.acelerazg.modelos

import groovy.transform.ToString

@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class Empresa extends Pessoa {
    String cnpj
    String pais
    HashMap<Integer, Vaga> listaVagas

    Empresa(int id, String nome, String email, String estado, String cep, String descricao, String cnpj, String pais) {
        super(id, nome, email, estado, cep, descricao)
        this.cnpj = cnpj
        this.pais = pais
        this.listaVagas = new HashMap<>()
    }
}
