package com.attornatus.attornatus.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ResponseAddressDTO {
    @NotNull(message = "id cannot be blank")
    private Long id;

    @NotBlank(message = "street cannot be null")
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