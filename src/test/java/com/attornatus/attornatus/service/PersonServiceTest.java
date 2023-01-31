package com.attornatus.attornatus.service;

import com.attornatus.attornatus.dto.request.AddressAttachedPersonDTO;
import com.attornatus.attornatus.dto.request.CreatePersonDTO;
import com.attornatus.attornatus.entity.Person;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.exception.MultipleMainAddressException;
import com.attornatus.attornatus.repository.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class PersonServiceTest {
    PersonRepository personRepository = Mockito.mock(PersonRepository.class);

    private final PersonService personService = new PersonService(personRepository);

    @AfterEach
    void resetMocking() {
        Mockito.reset(personRepository);
    }

    @Test
    public void shouldCreateAPerson() throws MultipleMainAddressException, BusinessRuleException {
        List<AddressAttachedPersonDTO> addressesDTO = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000001", false));
        CreatePersonDTO createPersonDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(addressesDTO).build();

        personService.createPerson(createPersonDTO);

        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void shouldThrowAIllegalArgumentExceptionWhenCreateAPerson() {
        assertThrows(IllegalArgumentException.class, () -> personService.createPerson(null));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void shouldThrowABusinessRuleExceptionWhenCreateAPersonWithNullAddress() {
        CreatePersonDTO personDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();

        assertThrows(BusinessRuleException.class, () -> personService.createPerson(personDTO));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void shouldThrowABusinessRuleExceptionWhenCreateAPersonWithEmptyAddress() {
        CreatePersonDTO personDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(List.of()).build();

        assertThrows(BusinessRuleException.class, () -> personService.createPerson(personDTO));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void shouldThrowABusinessRuleExceptionWhenCreateAPersonWithoutMainAddress() {
        CreatePersonDTO personDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(List.of(new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", false))).build();

        assertThrows(BusinessRuleException.class, () -> personService.createPerson(personDTO));
        verify(personRepository, times(0)).save(any(Person.class));
    }
}