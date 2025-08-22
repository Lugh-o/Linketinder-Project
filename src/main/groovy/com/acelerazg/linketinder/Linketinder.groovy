package com.acelerazg.linketinder

import com.acelerazg.modelos.Candidato
import com.acelerazg.modelos.Empresa

class Linketinder {
    private HashSet<Candidato> listaCandidatos
    private HashSet<Empresa> listaEmpresas

    Linketinder() {
        listaCandidatos = new HashSet<>()
        listaEmpresas = new HashSet<>()
    }

    boolean addCandidato(Candidato candidato){
        if(candidato){
            listaCandidatos.add(candidato)
            return true
        }
        return false
    }

    boolean addEmpresa(Empresa empresa){
        if(empresa){
            listaEmpresas.add(empresa)
            return true
        }
        return false
    }

    HashSet<Candidato> getListaCandidatos() {
        return listaCandidatos
    }

    HashSet<Empresa> getListaEmpresas() {
        return listaEmpresas
    }


}
