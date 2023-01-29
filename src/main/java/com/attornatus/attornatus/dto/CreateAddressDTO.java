package com.attornatus.attornatus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateAddressDTO {
    @NotBlank(message = "street cannot be blank")
    private String street;

    @NotBlank(message = "number cannot be blank")
    private String number;

    @NotBlank(message = "city cannot be blank")
    private String city;

    @NotBlank(message = "state cannot be blank")
    private String state;

    @NotBlank(message = "cep cannot be blank")
    private String cep;

    @NotNull(message = "main cannot be null")
    private Boolean main;

    @NotNull(message = "personId cannot be null")
    private Long personId;
}
