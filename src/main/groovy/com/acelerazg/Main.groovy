package com.acelerazg

import com.acelerazg.factory.LinketinderFactory
import com.acelerazg.linketinder.Linketinder
import com.acelerazg.modelos.Competencia
import com.acelerazg.modelos.Curtida
import com.acelerazg.modelos.Vaga

// Feito por Lucas Carneiro Falcao
class Main {
    static void main(String[] args) {
        println "Bem vindo ao Linketinder!"
        Linketinder app = new Linketinder()
        LinketinderFactory.generateData(app)

        def appInput = ""
        try (Scanner appScanner = new Scanner(System.in)) {
            while (appInput != "q") {
                println "Insira a operacao que deseja realizar: \n" +
                        "1 - Listar Candidatos \n" +
                        "2 - Listar Empresas \n" +
                        "3 - Adicionar Candidato \n" +
                        "4 - Adicionar Empresa\n" +
                        "5 - Adicionar vaga a uma empresa\n" +
                        "6 - Curtir vaga como candidato\n" +
                        "7 - Curtir candidato como empresa\n" +
                        "8 - Listar matches de uma vaga\n" +
                        "q - Sair da Aplicacao\n"

                appInput = appScanner.nextLine()
                switch (appInput) {
                    case "1":
                        app.getListaCandidatos().each { println it }
                        break

                    case "2":
                        app.getListaEmpresas().each { println it }
                        break

                    case "3":
                        println "Nome: "
                        String nomeCandidato = appScanner.nextLine()
                        println "Email: "
                        String emailCandidato = appScanner.nextLine()
                        println "Estado: "
                        String estadoCandidato = appScanner.nextLine()
                        println "CEP: "
                        String cepCandidato = appScanner.nextLine()
                        println "Descricao: "
                        String descricaoCandidato = appScanner.nextLine()
                        ArrayList<Competencia> competenciasCandidato = lerCompetencia(appScanner)
                        println "CPF: "
                        String cpfCandidato = appScanner.nextLine()
                        int idadeCandidato = lerInteiroPositivo(appScanner, 'Idade: ')
                        app.addCandidato(nomeCandidato, emailCandidato, estadoCandidato, cepCandidato, descricaoCandidato, competenciasCandidato, cpfCandidato, idadeCandidato)
                        println "Candidato Adicionado com sucesso"
                        break

                    case "4":
                        println "Nome: "
                        String nomeEmpresa = appScanner.nextLine()
                        println "Email: "
                        String emailEmpresa = appScanner.nextLine()
                        println "Estado: "
                        String estadoEmpresa = appScanner.nextLine()
                        println "CEP: "
                        String cepEmpresa = appScanner.nextLine()
                        println "Descricao: "
                        String descricaoEmpresa = appScanner.nextLine()
                        println "CNPJ: "
                        String cnpjEmpresa = appScanner.nextLine()
                        println "Pais: "
                        String paisEmpresa = appScanner.nextLine()
                        app.addEmpresa(nomeEmpresa, emailEmpresa, estadoEmpresa, cepEmpresa, descricaoEmpresa, cnpjEmpresa, paisEmpresa)
                        println "Empresa Adicionada com sucesso"
                        break

                    case "5":
                        int idEmpresa = lerId(appScanner, app.getListaEmpresas(), "Id Empresa:")
                        println "Nome da vaga: "
                        String nomeVaga = appScanner.nextLine()
                        ArrayList<Competencia> competenciasVagas = lerCompetencia(appScanner)
                        app.addVaga(idEmpresa, nomeVaga, competenciasVagas)
                        println "Vaga Adicionada com sucesso"
                        break

                    case "6":
                        app.getListaCandidatos().each { println it }
                        int idCandidato = lerId(appScanner, app.getListaCandidatos(), "Id Candidato: ")

                        int idEmpresa = lerId(appScanner, app.getListaEmpresas(), "Id Empresa:")
                        HashMap<Integer, Vaga> listaVagas = app.getVagasEmpresa(idEmpresa)
                        if(!listaVagas){
                            println "Essa empresa nao possui vagas"
                            break
                        }
                        listaVagas.each {println it.value}

                        int idVaga = lerId(appScanner, app.getVagasEmpresa(idEmpresa), "Id Vaga: ")

                        app.addCurtida(idCandidato, idEmpresa, idVaga)
                        println "Curtida adicionada com sucesso"
                        break

                    case "7":
                        int idEmpresa = lerId(appScanner, app.getListaEmpresas(), "Id Empresa:")
                        HashMap<Integer, Vaga> listaVagas = app.getVagasEmpresa(idEmpresa)
                        if(!listaVagas){
                            println "Essa empresa nao possui vagas"
                            break
                        }
                        listaVagas.each {println it}

                        int idVaga = lerId(appScanner, app.getVagasEmpresa(idEmpresa), "Id Vaga: ")
                        HashMap<Integer, Curtida> listaCurtidas = app.getCurtidasVaga(idEmpresa, idVaga)
                        if(!listaCurtidas){
                            println "Essa vaga nao possui curtidas"
                            break
                        }
                        listaCurtidas.each() {println it}
                        int idCurtida = lerIdCurtida(appScanner, listaCurtidas)
                        app.reciprocarCurtida(idEmpresa, idCurtida)
                        println "Candidato curtido com sucesso"
                        break
                    case "8":
                        int idEmpresa = lerId(appScanner, app.getListaEmpresas(), "Id Empresa:")
                        HashMap<Integer, Vaga> listaVagas = app.getVagasEmpresa(idEmpresa)
                        if(!listaVagas){
                            println "Essa empresa nao possui vagas"
                            break
                        }
                        listaVagas.each {println it.value}
                        int idVaga = lerId(appScanner, app.getVagasEmpresa(idEmpresa), "Id Vaga: ")
                        HashMap<Integer, Curtida> listaMatches = app.getCurtidasVaga(idEmpresa, idVaga).findAll() {it.value.empresa != null} as HashMap
                        if(!listaMatches){
                            println "Essa vaga nao possui matches"
                            break
                        }
                        listaMatches.each {
                            println "Vaga ID $it.value.vaga.id da Empresa ID $it.value.empresa.id -> Match com o Candidato ID $it.value.candidato.id"
                        }
                        break
                    case "q":
                        println "Saindo da Aplicacao"
                        return

                    default:
                        println "Valor invalido. Tente novamente"
                        break
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    private static int lerId(Scanner appScanner, HashMap lista, String mensagem){
        while(true){
            try {
                int id = lerInteiroPositivo(appScanner, mensagem)
                if(!lista.get(id)) throw new Exception()
                return id
            } catch(Exception ignored){
                println "Id invalido. Tente novamente"
            }
        }
    }

    private static int lerInteiroPositivo(Scanner appScanner, String mensagem) {
        int num
        while (true) {
            println mensagem
            try {
                num = appScanner.nextLine().toInteger()
                if(num <= 0) throw new Exception()
                break
            } catch (Exception ignored) {
                println "Valor invalido. Tente novamente"
            }
        }
        return num
    }

    private static ArrayList<Competencia> lerCompetencia(Scanner appScanner) {
        Set<Competencia> competencias = new LinkedHashSet<>()
        while (true) {
            println "Adicionar Competencias: "
            Competencia.values().eachWithIndex { comp, i ->
                println "${i + 1}: ${comp.name().replace("_", " ").capitalize()}"
            }
            println "f: Finalizar"

            String input = appScanner.nextLine()
            if (input == "f") {
                return new ArrayList<>(competencias)
            }

            try {
                int indice = input.toInteger() - 1
                Competencia selecionada = Competencia.values()[indice]
                competencias.add(selecionada)
            } catch (Exception ignored) {
                println "Valor invalido. Tente novamente"
            }
        }
    }
}