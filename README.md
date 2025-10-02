# Linketinder

Uma aplicação que mescla as funcionalidades do Linkedin e do Tinder, para Candidatos darem Match com empresas. Atualmente possui um backend e um frontend isolado, cada um com funcionalidades parciais. O backend está integrado a um banco de dados PostgreSQL.

## Funcionalidades

-   Criação e Leitura de Candidatos e Empresas
-   Empresas podem ter vagas
-   Likes e matches de vagas entre empresas e candidatos

## Backend

### Requisitos

-   Java 8+
-   Groovy 4.0.14+
-   PostgreSQL

### Como executar

Após o clone, entre na pasta:

```
cd Linketinder-Project/backend
```

Crie um arquivo .env baseado no .env.example e insira as credenciais do seu banco de dados
```
cp .env.example .env
```

É necessário realizar a migração inicial do banco de dados, que atualmente se encontra em 'backend/src/main/groovy/com/acelerazg/persistence/Migrate.groovy'. Posteriormente será elaborado um script utilizando gradle para esse passo.

Para compilar o projeto:

```
./gradlew build
```

Para rodar:

```
java -jar build/libs/Linketinder-Project-1.0-SNAPSHOT.jar
```

Para executar os testes unitários:

```
./gradlew test
```

## Frontend

### Requisitos

-   Node.Js 20.19+

### Como executar

Após o clone, entre na pasta:

```
cd Linketinder-Project/frontend
```

Para instalar as dependências:

```
npm install
```

Para executar:

```
npm run preview
```

Então a aplicação será servida em http://localhost:4173/

## Banco de Dados

Modelo do banco de dados utilizando a plataforma [dbdiagram](https://dbdiagram.io).
<img src="./DER.png" width="900">

Dentro da pasta de backend/src/main/resources/db existem 3 arquivos, schema, data e queries, com a definição do banco de dados, a inserção de dados dummy e consultas de vagas do ponto de vista de candidatos, likes que uma empresa recebeu e matches que ocorreram no sistema.

## Licença

Este projeto é livre para uso pessoal e acadêmico. Sinta-se à vontade para clonar, modificar e melhorar.
