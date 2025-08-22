package com.acelerazg.modelos

class Empresa extends Pessoa{
    private String cnpj
    private String pais

    Empresa(String nome, String email, String estado, String cep, String descricao, ArrayList<Competencia> competencias, String cnpj, String pais) {
        super(nome, email, estado, cep, descricao, competencias)
        this.cnpj = cnpj
        this.pais = pais
    }

    String getCnpj() {
        return cnpj
    }

    void setCnpj(String cnpj) {
        this.cnpj = cnpj
    }

    String getPais() {
        return pais
    }

    void setPais(String pais) {
        this.pais = pais
    }

    @Override
    String toString() {
        return "Empresa{" +
                super.toString() +
                ", cnpj='" + cnpj + '\'' +
                ", pais='" + pais + '\'' +
                "} " + super.toString();
    }
}
