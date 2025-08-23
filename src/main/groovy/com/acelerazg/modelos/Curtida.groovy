package com.acelerazg.modelos

class Curtida {
    private final int id
    private Candidato candidato
    private Empresa empresa
    private Vaga vaga

    Curtida(int id, Candidato candidato, Vaga vaga) {
        this.id = id
        this.candidato = candidato
        this.vaga = vaga
    }

    Candidato getCandidato() {
        return candidato
    }


    void setCandidato(Candidato candidato) {
        this.candidato = candidato
    }

    Empresa getEmpresa() {
        return empresa
    }

    void setEmpresa(Empresa empresa) {
        this.empresa = empresa
    }

    Vaga getVaga() {
        return vaga
    }

    void setVaga(Vaga vaga) {
        this.vaga = vaga
    }

    int getId() {
        return id
    }

    @Override
    String toString() {
        return "Curtida{" +
                "id='$id'" +
                ", candidato='$candidato'" +
                ", empresa='$empresa'" +
                ", vaga='$vaga'" +
                '}';
    }
}
