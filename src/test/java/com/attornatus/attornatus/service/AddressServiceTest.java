package com.attornatus.attornatus.service;

import com.attornatus.attornatus.dto.request.CreateAddressDTO;
import com.attornatus.attornatus.entity.Address;
import com.attornatus.attornatus.entity.Person;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.repository.AddressRepository;
import com.attornatus.attornatus.repository.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddressServiceTest {
    AddressRepository addressRepository = Mockito.mock(AddressRepository.class);
    PersonRepository personRepository = Mockito.mock(PersonRepository.class);

    private final AddressService addressService = new AddressService(addressRepository, personRepository);

    @AfterEach
    void resetMocking() {
        Mockito.reset(addressRepository);
        Mockito.reset(personRepository);
    }

    @Test
    public void shouldCreateAAddress() throws BusinessRuleException {
        CreateAddressDTO createAddressDTO = CreateAddressDTO.builder().street("Rua da Igreja")
                .number("12")
                .city("São Paulo")
                .state("SP")
                .cep("00000000")
                .main(false)
                .personId(1L).build();

        Address address = new Address(1L, "Rua da Igreja", "12", "São Paulo", "SP", "00000000", false, new Person());
        Person person = new Person(1L, "John Doe", LocalDate.parse("2000-01-01"), new ArrayList<>());

        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        person.setAddresses(addresses);
        address.setPerson(person);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        addressService.createAddress(createAddressDTO);

        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    public void shouldCreateAMainAddress() throws BusinessRuleException {
        CreateAddressDTO createAddressDTO = CreateAddressDTO.builder().street("Rua da Igreja")
                .number("12")
                .city("São Paulo")
                .state("SP")
                .cep("00000000")
                .main(true)
                .personId(1L).build();

        Address address = new Address(1L, "Rua da Igreja", "12", "São Paulo", "SP", "00000000", true, new Person());
        Person person = new Person(1L, "John Doe", LocalDate.parse("2000-01-01"), new ArrayList<>());

        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        person.setAddresses(addresses);
        address.setPerson(person);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        addressService.createAddress(createAddressDTO);

        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    public void shouldThrowAIllegalArgumentExceptionWhenCreateAAddress() {
        assertThrows(IllegalArgumentException.class, () -> addressService.createAddress(null));
        verify(addressRepository, times(0)).save(any(Address.class));
    }

    @Test
    public void shouldThrowABusinessRuleExceptionWhenCreateAAddressWithNotFoundPerson() {
        CreateAddressDTO createAddressDTO = CreateAddressDTO.builder().street("Rua da Igreja")
                .number("12")
                .city("São Paulo")
                .state("SP")
                .cep("00000000")
                .main(true)
                .personId(0L).build();

        assertThrows(BusinessRuleException.class, () -> addressService.createAddress(createAddressDTO));
        verify(addressRepository, times(0)).save(any(Address.class));
    }
}
