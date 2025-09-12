package com.acelerazg.factory

import com.acelerazg.linketinder.Linketinder
import com.acelerazg.modelos.Competencia

/**
 * Classe factory para popular uma instância do aplicativo Linketinder com dados de exemplo.
 *
 * <p>Possui metodo estático para adicionar candidatos,
 * empresas, vagas e curtidas pré-definidas para testes.</p>
 *
 */
class LinketinderFactory {
    static void generateData(Linketinder app) {
        app.addCandidato(
                "Lucas Falcao", "lucas@email.com", "SP", "01000-000",
                "Desenvolvedor Fullstack", [Competencia.JAVA, Competencia.SPRING_BOOT] as ArrayList,
                "12345678900", 25
        )

        app.addCandidato("Ana Souza", "ana@email.com", "RJ", "20000-000",
                "Analista de Dados", [Competencia.PYTHON, Competencia.KAFKA] as ArrayList,
                "98765432100", 28
        )

        app.addCandidato("Carlos Lima", "carlos@email.com", "MG", "30000-000",
                "DevOps", [Competencia.DOCKER, Competencia.JENKINS, Competencia.LINUX] as ArrayList,
                "11223344550", 32
        )

        app.addCandidato("Mariana Alves", "mariana@email.com", "SP", "01000-001",
                "Frontend Developer", [Competencia.PHP, Competencia.LARAVEL, Competencia.SWAGGER] as ArrayList,
                "22334455660", 24
        )

        app.addCandidato("Rafael Costa", "rafael@email.com", "RS", "90000-000",
                "Backend Developer", [Competencia.DOTNET, Competencia.MICRONAUT, Competencia.POSTGRESQL] as ArrayList,
                "33445566770", 29
        )

        app.addEmpresa("Tech Solutions", "contato@techsolutions.com", "SP", "01001-000",
                "Consultoria em Tecnologia", "12345678000100", "Brasil"
        )
        app.addVaga(1, "Programador Java Pleno", [Competencia.JAVA, Competencia.DOCKER, Competencia.SPRING_BOOT] as ArrayList)
        app.addVaga(1, "Programador Junior .NET", [Competencia.DOTNET, Competencia.SWAGGER] as ArrayList)
        app.addCurtida(1, 1, 1)
        app.reciprocarCurtida(1, 1)

        app.addEmpresa("DataCorp", "vendas@datacorp.com", "RJ", "20001-000",
                "Análise de Dados e BI", "98765432000100", "Brasil"
        )

        app.addEmpresa("DevOps Inc", "info@devopsinc.com", "MG", "30001-000",
                "Serviços DevOps", "11223344000100", "Brasil"
        )

        app.addEmpresa("Web Creators", "contato@webcreators.com", "SP", "01002-000",
                "Desenvolvimento Frontend", "22334455000100", "Brasil"
        )

        app.addEmpresa("Backend Solutions", "contato@backend.com", "RS", "90001-000",
                "Soluções Backend", "33445566000100", "Brasil"
        )
    }
}
