package com.acelerazg.linketinder

import com.acelerazg.modelos.Candidato
import com.acelerazg.modelos.Competencia
import com.acelerazg.modelos.Curtida
import com.acelerazg.modelos.Empresa
import com.acelerazg.modelos.Vaga

class Linketinder {
    HashMap<Integer, Candidato> listaCandidatos
    HashMap<Integer,Empresa> listaEmpresas
    HashMap<Integer,Curtida> listaCurtidas
    int idCandidatosAtual = 1
    int idEmpresasAtual = 1
    int idVagaAtual = 1
    int idCurtidaAtual = 1

    Linketinder() {
        listaCandidatos = new HashMap<>()
        listaEmpresas = new HashMap<>()
        listaCurtidas = new HashMap<>()
    }

    void addCandidato(String nome, String email, String estado, String cep, String descricao, ArrayList<Competencia> competencias, String cpf, int idade){
        Candidato candidato = new Candidato(idCandidatosAtual, nome, email, estado, cep, descricao, competencias, cpf, idade)
        if(candidato) listaCandidatos.put(idCandidatosAtual++, candidato)
    }

    void addEmpresa(String nome, String email, String estado, String cep, String descricao, String cnpj, String pais){
        Empresa empresa = new Empresa(idEmpresasAtual, nome, email, estado, cep, descricao, cnpj, pais)
        if(empresa) listaEmpresas.put(idEmpresasAtual++, empresa)
    }

    void addVaga(int idEmpresa, String nome, ArrayList<Competencia> competencias){
        Empresa empresa = listaEmpresas.get(idEmpresa)
        if(empresa){
            Vaga vaga = new Vaga(idVagaAtual, nome, competencias)
            if(vaga) empresa.listaVagas.put(idVagaAtual++, vaga)
        }
    }

    void addCurtida(int idCandidato, int idEmpresa, int idVaga){
        Candidato candidato = getListaCandidatos().get(idCandidato)
        if(candidato){
            Vaga vaga = getVagasEmpresa(idEmpresa).get(idVaga)
            if(vaga){
                Curtida curtida = new Curtida(idCurtidaAtual, candidato, vaga)
                listaCurtidas.put(idCurtidaAtual++, curtida)
            }
        }
    }

    void reciprocarCurtida(int idEmpresa, int idCurtida){
        Empresa empresa = getListaEmpresas().get(idEmpresa)
        if(empresa){
            Curtida curtida = getListaCurtidas().get(idCurtida)
            curtida.setEmpresa(empresa)
        }
    }

    HashMap<Integer, Curtida> getCurtidasVaga(int idEmpresa, int idVaga){
        Vaga vaga = getVagasEmpresa(idEmpresa).get(idVaga)
        return listaCurtidas.findAll(){it.value.vaga == vaga} as HashMap
    }

    HashMap<Integer, Vaga> getVagasEmpresa(int idEmpresa) {
        Empresa empresa = listaEmpresas.get(idEmpresa)
        if(!empresa) return null
        return empresa.getListaVagas()
    }
}
