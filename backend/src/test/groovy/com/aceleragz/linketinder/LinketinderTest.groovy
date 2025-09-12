package com.aceleragz.linketinder

import com.acelerazg.linketinder.Linketinder
import com.acelerazg.modelos.Competencia
import spock.lang.Specification

class LinketinderTest extends Specification {

    def "adicionarCandidatoComCamposCorretos"(){
        given:
            Linketinder app = new Linketinder()
        when:
            app.addCandidato("Ana Souza", "ana@email.com", "RJ", "20000-000",
                    "Analista de Dados", [Competencia.PYTHON, Competencia.KAFKA] as ArrayList,
                    "98765432100", 28)
        then:
            app.getListaCandidatos().size() == old(app.getListaCandidatos().size() + 1)
    }

    def "adicionarEmpresaComCamposCorretos"(){
        given:
            Linketinder app = new Linketinder()
        when:
            app.addEmpresa("Tech Solutions", "contato@techsolutions.com", "SP", "01001-000",
                "Consultoria em Tecnologia", "12345678000100", "Brasil")
        then:
            app.getListaEmpresas().size() == old(app.getListaEmpresas().size() + 1)
    }

}
