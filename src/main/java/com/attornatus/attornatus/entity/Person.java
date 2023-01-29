package com.attornatus.attornatus.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
    @NotBlank(message = "name cannot be blank")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "birthDate cannot be blank")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @JsonIgnore
    @OneToMany(mappedBy = "person")
    private List<Address> addresses;
}
