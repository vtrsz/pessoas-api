package com.attornatus.attornatus.entity;

import jakarta.persistence.*;

@Entity(name = "address")
@Table(name = "address")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id_fk", referencedColumnName = "id")
    private Person person;
}
