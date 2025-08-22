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

        // =========== DADOS PRE-CADASTRADOS =============//
            app.addCandidato(new Candidato(
            "Lucas Falcao", "lucas@email.com", "SP", "01000-000",
            "Desenvolvedor Fullstack", [Competencia.JAVA, Competencia.SPRING_BOOT] as ArrayList,
            "12345678900", 25
        ))

        app.addCandidato(new Candidato(
            "Ana Souza", "ana@email.com", "RJ", "20000-000",
            "Analista de Dados", [Competencia.PYTHON, Competencia.KAFKA] as ArrayList,
            "98765432100", 28
        ))

        app.addCandidato(new Candidato(
            "Carlos Lima", "carlos@email.com", "MG", "30000-000",
            "DevOps", [Competencia.DOCKER, Competencia.JENKINS, Competencia.LINUX] as ArrayList,
            "11223344550", 32
        ))

        app.addCandidato(new Candidato(
            "Mariana Alves", "mariana@email.com", "SP", "01000-001",
            "Frontend Developer", [Competencia.PHP, Competencia.LARAVEL, Competencia.SWAGGER] as ArrayList,
            "22334455660", 24
        ))

        app.addCandidato(new Candidato(
            "Rafael Costa", "rafael@email.com", "RS", "90000-000",
            "Backend Developer", [Competencia.DOTNET, Competencia.MICRONAUT, Competencia.POSTGRESQL] as ArrayList,
            "33445566770", 29
        ))

        app.addEmpresa(new Empresa(
            "Tech Solutions", "contato@techsolutions.com", "SP", "01001-000",
            "Consultoria em Tecnologia", [Competencia.JAVA, Competencia.SPRING_BOOT] as ArrayList,
            "12345678000100", "Brasil"
        ))

        app.addEmpresa(new Empresa(
            "DataCorp", "vendas@datacorp.com", "RJ", "20001-000",
            "Análise de Dados e BI", [Competencia.PYTHON, Competencia.KAFKA] as ArrayList,
            "98765432000100", "Brasil"
        ))

        app.addEmpresa(new Empresa(
            "DevOps Inc", "info@devopsinc.com", "MG", "30001-000",
            "Serviços DevOps", [Competencia.DOCKER, Competencia.JENKINS, Competencia.LINUX] as ArrayList,
            "11223344000100", "Brasil"
        ))

        app.addEmpresa(new Empresa(
            "Web Creators", "contato@webcreators.com", "SP", "01002-000",
            "Desenvolvimento Frontend", [Competencia.PHP, Competencia.LARAVEL, Competencia.SWAGGER] as ArrayList,
            "22334455000100", "Brasil"
        ))

        app.addEmpresa(new Empresa(
            "Backend Solutions", "contato@backend.com", "RS", "90001-000",
            "Soluções Backend", [Competencia.DOTNET, Competencia.MICRONAUT, Competencia.POSTGRESQL] as ArrayList,
            "33445566000100", "Brasil"
        ))
        // ============= //

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
                        app.getListaCandidatos().each { println it }
                        break
                    case "2":
                        println app.getListaEmpresas().each { println it }
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
            } catch (Exception ignored) {
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