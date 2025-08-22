package com.acelerazg

import com.acelerazg.linketinder.Linketinder
import com.acelerazg.modelos.Candidato
import com.acelerazg.modelos.Competencia
import com.acelerazg.modelos.Empresa

// Feito por Lucas Carneiro Falcao
class Main {
    static void main(String[] args) {
        println "Bem vindo ao Linketinder!"
        Linketinder app = new Linketinder()
        def appInput = ""
        try (Scanner appScanner = new Scanner(System.in)) {
            while (appInput != "q") {
                println "Insira a operacao que deseja realizar: \n" +
                        "1 - Listar Candidatos \n" +
                        "2 - Listar Empresas \n" +
                        "3 - Adicionar Candidato \n" +
                        "4 - Adicionar Empresa\n" +
                        "q - Sair da Aplicacao\n"

                appInput = appScanner.nextLine()
                switch (appInput) {
                    case "1":
                        println app.getListaCandidatos()
                        break
                    case "2":
                        println app.getListaEmpresas()
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
                        int idadeCandidato = lerIdade(appScanner)

                        app.addCandidato(new Candidato(nomeCandidato, emailCandidato, estadoCandidato, cepCandidato, descricaoCandidato, competenciasCandidato, cpfCandidato, idadeCandidato))
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
                        ArrayList<Competencia> competenciasEmpresa = lerCompetencia(appScanner)

                        println "CNPJ: "
                        String cnpjEmpresa = appScanner.nextLine()
                        println "Pais: "
                        String paisEmpresa = appScanner.nextLine()
                        app.addEmpresa(new Empresa(nomeEmpresa, emailEmpresa, estadoEmpresa, cepEmpresa, descricaoEmpresa, competenciasEmpresa, cnpjEmpresa, paisEmpresa))
                        println "Empresa Adicionada com sucesso"

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

    private static int lerIdade(Scanner appScanner) {
        int idade
        while (true) {
            println "Idade: "
            try {
                idade = appScanner.nextLine().toInteger()
                break
            } catch (Exception e) {
                println "Valor invalido. Tente novamente"
            }
        }
        return idade
    }

    private static ArrayList<Competencia> lerCompetencia(Scanner appScanner) {
        Set<Competencia> competencias = new LinkedHashSet<>()
        while (true) {
            println "Adicionar Competencias: \n" +
                    "1: Java\n" +
                    "2: Python\n" +
                    "3: C sharp\n" +
                    "4: Spring boot\n" +
                    "5: Php\n" +
                    "6: Laravel\n" +
                    "7: Docker\n" +
                    "8: Jenkins\n" +
                    "9: Postgresql\n" +
                    "10: Mysql\n" +
                    "11: Swagger\n" +
                    "12: Kafka\n" +
                    "13: Micronaut\n" +
                    "14: Linux\n" +
                    "15: Dotnet\n" +
                    "f: Finalizar"

            String inputCompetencia = appScanner.nextLine()

            switch (inputCompetencia) {
                case "1": competencias.add(Competencia.JAVA); break
                case "2": competencias.add(Competencia.PYTHON); break
                case "3": competencias.add(Competencia.C_SHARP); break
                case "4": competencias.add(Competencia.SPRING_BOOT); break
                case "5": competencias.add(Competencia.PHP); break
                case "6": competencias.add(Competencia.LARAVEL); break
                case "7": competencias.add(Competencia.DOCKER); break
                case "8": competencias.add(Competencia.JENKINS); break
                case "9": competencias.add(Competencia.POSTGRESQL); break
                case "10": competencias.add(Competencia.MYSQL); break
                case "11": competencias.add(Competencia.SWAGGER); break
                case "12": competencias.add(Competencia.KAFKA); break
                case "13": competencias.add(Competencia.MICRONAUT); break
                case "14": competencias.add(Competencia.LINUX); break
                case "15": competencias.add(Competencia.DOTNET); break
                case "f":
                    return new ArrayList<>(competencias)
                default: println "Opção inválida"; break
            }
        }
    }
}