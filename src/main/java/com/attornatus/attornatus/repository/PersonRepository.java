package com.attornatus.attornatus.repository;

import com.attornatus.attornatus.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByOrderByIdAsc();
}