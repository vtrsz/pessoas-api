package com.attornatus.attornatus.entity;

import com.attornatus.attornatus.dto.response.ResponseAddressAttachedPersonDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Entity(name = "address")
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private Boolean main;

    @ManyToOne
    @JoinColumn(name = "person_id_fk", referencedColumnName = "id")
    private Person person;

    public ResponseAddressAttachedPersonDTO toDto() {
        ResponseAddressAttachedPersonDTO address = new ResponseAddressAttachedPersonDTO();
        BeanUtils.copyProperties(this, address);
        return address;
    }
}
