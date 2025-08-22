package com.acelerazg.modelos

class Candidato extends Pessoa {
    private String cpf
    private int idade

    Candidato(String nome, String email, String estado, String cep, String descricao, ArrayList<Competencia> competencias, String cpf, int idade) {
        super(nome, email, estado, cep, descricao, competencias)
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
                ". cpf='" + cpf + '\'' +
                ", idade=" + idade +
                '}'
    }
}
