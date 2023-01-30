package com.attornatus.attornatus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ValidationErrorResponseDTO {
    String field;
    String error;
}