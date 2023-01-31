package com.attornatus.attornatus.dto.request;

import com.attornatus.attornatus.entity.Address;
import com.attornatus.attornatus.entity.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreatePersonDTO {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotNull(message = "birthDate cannot be blank")
    private LocalDate birthDate;

    @NotNull(message = "addresses cannot be null")
    private List<AddressAttachedPersonDTO> addresses;

    public Person toEntity() {
        Person person = new Person();
        BeanUtils.copyProperties(this, person);

        List<Address> addresses = this.getAddresses().stream()
                .map(addressAttachedPersonDTO -> addressAttachedPersonDTO.toEntity(person))
                .collect(Collectors.toList());

        person.setAddresses(addresses);
        return person;
    }
}
