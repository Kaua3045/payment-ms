# Ecommerce

## Ferramentas utilizadas
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)

## Sobre

Este projeto processa pagamentos recebendo um evento orderPlacedEvent com os detalhes do pedido. Ao receber esse evento, geramos um novo evento que aciona o gateway de pagamento. A cobrança é realizada no crédito ou débito e, conforme o resultado, retornamos um evento PaymentApprovedEvent em caso de sucesso ou PaymentFailedEvent em caso de falha.

- Porquê decidiu fazer esse projeto?
    - Para aprender mais sobre kafka, arquitetura orientada a eventos, event Sourcing e CQRS.

- Quais foram os desafios de implementá-lo?
    - O maior desafio foi entender como funciona toda a arquitetura orientada a eventos e o event sourcing, também usamos um banco postgres fazendo com que snapshots e salvar os eventos no banco exigisse um pouco mais de conhecimento, para armazenar da forma correta. 

- O que eu aprendi com ele?
    - Aprendi como funciona a arquitetura orientada a eventos, event sourcing e CQRS, também aprendi como funciona o kafka e como ele pode ser usado para processar eventos de forma assíncrona.

## Tabela de conteúdos

- [Arquitetura](#arquitetura)
- [Requsitos para rodar o projeto](#requisitos)
- [Instruções para executar o projeto](#instruções-para-executar-o-projeto)
- [Contribua com o projeto](#contribuindo-com-o-projeto)
- [Changelog](#changelog)

## Arquitetura

![Circulo da clean architecture](doc/imagens/clean-arch-circle)

**Camadas da aplicação**

*Domain, é a camada onde se encontra as regras de negócio, validações e as interfaces gateways (abstração dos métodos do banco dedados, são usadas para remover o acomplamento com o banco de dados)*

*Application, é a camada que contem todos os casos de uso (criar um usuário, pegar um usuário pelo id, atualizar um usuário, deletar um usuário, esse é famoso CRUD) e contem a integração com o gateway do banco de dados*

*Infrastructure, é a camada responsável por conectar tudo, o usuário com a application e domain layer, contem a conexão com o banco de dados, entidades do banco e as rotas*

## Requisitos para rodar o projeto

1. Docker e docker-compose
2. Java e JDK 17

## Instruções para executar o projeto

1. Baixe a aplicação e instale as dependências:
```bash
# Baixando o projeto e acessando o diretorio
git clone https://github.com/Kaua3045/payment-ms.git cd payment-ms

# Baixando as dependências
./gradlew dependencies  
```

2. Antes de executar a aplicação, você precisa configurar o arquivo .env.example, depois renomeie ele para .env

3. Agora inicie o container do banco de dados:
```bash
# Execute o container do banco de dados
docker-compose -f docker-compose-dev.yml up -d
```

4. Agora inicie a aplicação:
```bash
# Iniciando a aplicação
./gradlew bootRun
```
5. A url base da aplicação é: *localhost:8080/*

## Contribuindo com o projeto

Para contribuir com o projeto, veja mais informações em [CONTRIBUTING](doc/CONTRIBUTING.md)

## Changelog

Para ver as últimas alterações do projeto, acesse [AQUI](doc/changelog.md)

## Configurações para dev
After cloning project add commit-msg hook in your git path
```shell
    git config core.hooksPath .githooks
```
