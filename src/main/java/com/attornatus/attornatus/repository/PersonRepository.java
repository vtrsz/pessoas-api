package com.attornatus.attornatus.repository;

import com.attornatus.attornatus.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
