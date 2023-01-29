package com.attornatus.attornatus.repository;

import com.attornatus.attornatus.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}