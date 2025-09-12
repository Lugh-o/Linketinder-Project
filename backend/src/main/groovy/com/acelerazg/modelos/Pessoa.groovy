package com.acelerazg.modelos

abstract class Pessoa {
    final int id
    String nome
    String email
    String estado
    String cep
    String descricao

    Pessoa(int id, String nome, String email, String estado, String cep, String descricao) {
        this.id = id
        this.nome = nome
        this.email = email
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
    }
}
