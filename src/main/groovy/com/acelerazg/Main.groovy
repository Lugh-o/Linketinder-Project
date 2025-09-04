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
                        String nomeCandidato = lerStringNaoVazia(appScanner, "Nome: ", "O nome do candidato nao pode ser vazio.")
                        String emailCandidato = lerStringNaoVazia(appScanner, "Email: ", "O email do candidato nao pode ser vazio.")
                        String estadoCandidato = lerStringNaoVazia(appScanner, "Estado: ", "O estado do candidato nao pode ser vazio.")
                        String cepCandidato = lerCep(appScanner)
                        String descricaoCandidato = lerStringNaoVazia(appScanner, "Descricao: ", "A descricaoo do candidato nao pode ser vazia.")
                        ArrayList<Competencia> competenciasCandidato = lerCompetencia(appScanner)
                        String cpfCandidato = lerCpf(appScanner)
                        int idadeCandidato = lerInteiroPositivo(appScanner, 'Idade: ')

                        app.addCandidato(nomeCandidato, emailCandidato, estadoCandidato, cepCandidato, descricaoCandidato, competenciasCandidato, cpfCandidato, idadeCandidato)
                        println "Candidato Adicionado com sucesso"
                        break

                    case "4":
                        String nomeEmpresa = lerStringNaoVazia(appScanner, "Nome: ", "O nome da empresa nao pode ser vazio.")
                        String emailEmpresa = lerStringNaoVazia(appScanner, "Email: ", "O email da empresa nao pode ser vazio.")
                        String estadoEmpresa = lerStringNaoVazia(appScanner, "Estado: ", "O estado da empresa nao pode ser vazio.")
                        String cepEmpresa = lerCep(appScanner)
                        String descricaoEmpresa = lerStringNaoVazia(appScanner, "Descricao: ", "A descricao da empresa nao pode ser vazia.")
                        String cnpjEmpresa = lerCnpj(appScanner)
                        String paisEmpresa = lerStringNaoVazia(appScanner, "Pais: ", "O pais da empresa nao pode ser vazio.")

                        app.addEmpresa(nomeEmpresa, emailEmpresa, estadoEmpresa, cepEmpresa, descricaoEmpresa, cnpjEmpresa, paisEmpresa)
                        println "Empresa Adicionada com sucesso"
                        break

                    case "5":
                        int idEmpresa = lerId(appScanner, app.getListaEmpresas(), "Id Empresa:")
                        String nomeVaga = lerStringNaoVazia(appScanner, "Nome da Vaga: ", "O nome da vaga nao pode ser vazia.")
                        ArrayList<Competencia> competenciasVagas = lerCompetencia(appScanner)
                        app.addVaga(idEmpresa, nomeVaga, competenciasVagas)
                        println "Vaga Adicionada com sucesso"
                        break

                    case "6":
                        app.getListaCandidatos().each { println it }
                        int idCandidato = lerId(appScanner, app.getListaCandidatos(), "Id Candidato: ")

                        int idEmpresa = lerId(appScanner, app.getListaEmpresas(), "Id Empresa:")
                        HashMap<Integer, Vaga> listaVagas = app.getVagasEmpresa(idEmpresa)
                        if (!listaVagas) {
                            println "Essa empresa nao possui vagas"
                            break
                        }
                        listaVagas.each { println it.value }

                        int idVaga = lerId(appScanner, app.getVagasEmpresa(idEmpresa), "Id Vaga: ")

                        app.addCurtida(idCandidato, idEmpresa, idVaga)
                        println "Curtida adicionada com sucesso"
                        break

                    case "7":
                        int idEmpresa = lerId(appScanner, app.getListaEmpresas(), "Id Empresa:")
                        HashMap<Integer, Vaga> listaVagas = app.getVagasEmpresa(idEmpresa)
                        if (!listaVagas) {
                            println "Essa empresa nao possui vagas"
                            break
                        }
                        listaVagas.each { println it }

                        int idVaga = lerId(appScanner, app.getVagasEmpresa(idEmpresa), "Id Vaga: ")
                        HashMap<Integer, Curtida> listaCurtidas = app.getCurtidasVaga(idEmpresa, idVaga)
                        if (!listaCurtidas) {
                            println "Essa vaga nao possui curtidas"
                            break
                        }
                        listaCurtidas.each() { println it }
                        int idCurtida = lerIdCurtida(appScanner, listaCurtidas)
                        app.reciprocarCurtida(idEmpresa, idCurtida)
                        println "Candidato curtido com sucesso"
                        break
                    case "8":
                        int idEmpresa = lerId(appScanner, app.getListaEmpresas(), "Id Empresa:")
                        HashMap<Integer, Vaga> listaVagas = app.getVagasEmpresa(idEmpresa)
                        if (!listaVagas) {
                            println "Essa empresa nao possui vagas"
                            break
                        }
                        listaVagas.each { println it.value }
                        int idVaga = lerId(appScanner, app.getVagasEmpresa(idEmpresa), "Id Vaga: ")
                        HashMap<Integer, Curtida> listaMatches = app.getCurtidasVaga(idEmpresa, idVaga).findAll() { it.value.empresa != null } as HashMap
                        if (!listaMatches) {
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

    private static String lerStringNaoVazia(Scanner scanner, String prompt, String mensagemErro) {
        while (true) {
            println prompt
            String input = scanner.nextLine().trim()
            if (!input.isEmpty()) return input
            println mensagemErro
        }
    }

    private static String lerCep(Scanner scanner) {
        String cepRegex = /^\d{5}-\d{3}$/
        String cep

        while (true) {
            println "CEP (formato 12345-678):"
            cep = scanner.nextLine().trim()

            if (cep ==~ cepRegex) {
                return cep
            } else {
                println "CEP Invalido. Tente Novamente."
            }
        }
    }

    private static String lerCpf(Scanner scanner) {
        String cpfRegex = /\d{11}/
        String cpf
        while (true) {
            print "CPF (apenas numeros): "
            cpf = scanner.nextLine().trim()

            if (cpf.matches(cpfRegex)) {
                return cpf
            } else {
                println "CPF Invalido. Ele deve conter exatamente 11 digitos."
            }
        }
    }

    private static String lerCnpj(Scanner scanner) {
        String cnpjRegex = /\d{14}/
        String cnpj
        while (true) {
            print "CNPJ (apenas numeros): "
            cnpj = scanner.nextLine().trim()

            if (cnpj.matches(cnpjRegex)) {
                return cnpj
            } else {
                println "CNPJ Invalido. Ele deve conter exatamente 14 digitos."
            }
        }
    }


    private static int lerId(Scanner appScanner, HashMap lista, String mensagem) {
        while (true) {
            try {
                int id = lerInteiroPositivo(appScanner, mensagem)
                if (!lista.get(id)) throw new Exception()
                return id
            } catch (Exception ignored) {
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
                if (num <= 0) throw new Exception()
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