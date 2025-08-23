package com.acelerazg.modelos

class Empresa extends Pessoa{
    private String cnpj
    private String pais
    private HashMap<Integer, Vaga> listaVagas

    Empresa(int id, String nome, String email, String estado, String cep, String descricao, String cnpj, String pais) {
        super(id, nome, email, estado, cep, descricao)
        this.cnpj = cnpj
        this.pais = pais
        this.listaVagas = new HashMap<>()
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

    HashMap<Integer, Vaga> getListaVagas() {
        return listaVagas
    }

    void setListaVagas(HashMap<Integer, Vaga> listaVagas) {
        this.listaVagas = listaVagas
    }

    @Override
    String toString() {
        return "Empresa{" +
                super.toString() +
                ", cnpj='$cnpj'" +
                ", pais='$pais'" +
                ", listaVagas='" + Arrays.toString(listaVagas) + "'" +
                "} "
    }
}
