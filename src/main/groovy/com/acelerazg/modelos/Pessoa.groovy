package com.acelerazg.modelos

abstract class Pessoa {
    private String nome
    private String email
    private String estado
    private String cep
    private String descricao
    private ArrayList<Competencia> competencias

    Pessoa(String nome, String email, String estado, String cep, String descricao, ArrayList<Competencia> competencias) {
        this.nome = nome
        this.email = email
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
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

    ArrayList<Competencia> getCompetencias() {
        return competencias
    }

    void setCompetencias(ArrayList<Competencia> competencias) {
        this.competencias = competencias
    }

    void addCompetencia(Competencia competencia) {
        competencias.add(competencia)
    }

    @Override
    String toString() {
        return "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", estado='" + estado + '\'' +
                ", cep='" + cep + '\'' +
                ", descricao='" + descricao + '\'' +
                ", competencias=" + Arrays.toString(competencias)
    }
}
