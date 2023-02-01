package com.attornatus.attornatus.entity;

import com.attornatus.attornatus.dto.response.ResponseAddressAttachedPersonDTO;
import com.attornatus.attornatus.dto.response.ResponsePersonDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "person")
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Address> addresses;

    public ResponsePersonDTO toDto() {
        ResponsePersonDTO personDTO = new ResponsePersonDTO();
        BeanUtils.copyProperties(this, personDTO);

        List<ResponseAddressAttachedPersonDTO> addresses = this.getAddresses().stream()
                .map(Address::toDto)
                .collect(Collectors.toList());


        personDTO.setAddresses(addresses);
        return personDTO;
    }
}
