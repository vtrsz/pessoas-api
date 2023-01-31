package com.attornatus.attornatus.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ResponsePersonDTO {
    @NotNull(message = "id cannot be blank")
    private Long id;

    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotNull(message = "birthDate cannot be blank")
    private LocalDate birthDate;

    @NotNull(message = "addresses cannot be null")
    private List<ResponseAddressAttachedPersonDTO> addresses;
}
