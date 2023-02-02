# <p align="center">Pessoas API</p>


## Respostas sobre Qualidade de Código:

1. <strong>Durante a implementação de uma nova funcionalidade de software solicitada, quais
   critérios você avalia e implementa para garantia de qualidade de software?</strong>
<br><br>
   <strong>Resposta:</strong> Requisitos, faço uma análise e compreensão dos requisitos, realizo o desenho da arquitetura na minha cabeça, e já começo pensar em possíveis falhas, escalabilidade da implementação, manutenções futuras e segurança, implementação do código com boas práticas de codificação utilizando o padrão TDD para garantir a funcionalidade esteja de acordo com os requisitos e documentação clara e precisa.
<br><br>
2. <strong>Em qual etapa da implementação você considera a qualidade de software?</strong>
<br><br>
   <strong>Resposta:</strong> Em todas as etapas, desde a compreensão dos requisitos até o monitoramento de feedback pós-produção

## Tecnologias Utilizadas:

- Java 17
- H2 Database
- Spring Boot 3.0.2
- Maven
- Lombok
- OpenAPI Swagger 3
- Jackson
- Mockito

## Endpoints

<img src="https://i.imgur.com/ODTTGz4.png" alt="Endpoints" style="align-items: center"/>

Link da documentação: http://localhost:8080/ 
(em ambiente local utilizando a porta padrão do Spring Boot.)

### Exemplo de requests:

##### /api/person POST:
```json
{
  "name": "John Doe",
  "birthDate": "1990-01-27",
  "addresses": [
    {
      "street": "Rua Nova",
      "number": "100",
      "city": "São Paulo",
      "state": "SP",
      "cep": "00000000",
      "main": true
    }
  ]
}
```
##### /api/address/ POST:
```json
{
  "street": "Rua Nova",
  "number": "100",
  "city": "São Paulo",
  "state": "SP",
  "cep": "00000000",
  "main": true,
  "personId": 1
}
```
(Para obter mais exemplos consultar a documentação do Swagger)


## Como rodar o projeto?

Ter a <a href="https://www.oracle.com/br/java/technologies/downloads/#java17">JDK 17</a> instalada na sua maquina e uma IDE de preferência.