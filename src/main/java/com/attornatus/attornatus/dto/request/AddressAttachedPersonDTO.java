package com.attornatus.attornatus.dto.request;

import com.attornatus.attornatus.entity.Address;
import com.attornatus.attornatus.entity.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressAttachedPersonDTO {
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

    public Address toEntity(Person person) {
        Address address = new Address();
        BeanUtils.copyProperties(this, address);
        address.setPerson(person);

        return address;
    }
}
