package com.attornatus.attornatus.dto;

import com.attornatus.attornatus.entity.Address;
import com.attornatus.attornatus.entity.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreatePersonDTO {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "birthDate cannot be blank")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @NotNull(message = "address cannot be null")
    private List<Address> address;

    public Person toEntity() {
        Person person = new Person();
        BeanUtils.copyProperties(this, person);
        return person;
    }
}
