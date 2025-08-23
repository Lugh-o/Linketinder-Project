package com.acelerazg.modelos

abstract class Pessoa {
    private final int id
    private String nome
    private String email
    private String estado
    private String cep
    private String descricao

    Pessoa(int id, String nome, String email, String estado, String cep, String descricao) {
        this.id = id
        this.nome = nome
        this.email = email
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
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

    String getEmail() {
        return email
    }

    void setEmail(String email) {
        this.email = email
    }

    String getEstado() {
        return estado
    }

    void setEstado(String estado) {
        this.estado = estado
    }

    String getCep() {
        return cep
    }

    void setCep(String cep) {
        this.cep = cep
    }

    String getDescricao() {
        return descricao
    }

    void setDescricao(String descricao) {
        this.descricao = descricao
    }

    @Override
    String toString() {
        return "id='$id'" +
                ", nome='$nome'" +
                ", email='$email'" +
                ", estado='$estado'" +
                ", cep='$cep'" +
                ", descricao='$descricao'"
    }
}
