# <p align="center">Pessoas API</p>

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