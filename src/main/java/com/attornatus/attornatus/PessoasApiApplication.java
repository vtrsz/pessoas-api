package com.attornatus.attornatus;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Pessoas API", version = "1.0", description = "Desafio de Código proposto pela Attornatus"))
public class PessoasApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(PessoasApiApplication.class, args);
    }
}