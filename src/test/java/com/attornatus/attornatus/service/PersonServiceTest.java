package com.attornatus.attornatus.service;

import com.attornatus.attornatus.dto.request.AddressAttachedPersonDTO;
import com.attornatus.attornatus.dto.request.CreatePersonDTO;
import com.attornatus.attornatus.entity.Address;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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

        Person person = new Person(1L, "John Doe", LocalDate.parse("2000-01-01"), List.of(
        ));
        person.setAddresses(List.of(new Address(1L, "Rua da Lagoa", "10", "Recife", "PE", "0000000", true, person)));

        when(personRepository.save(any(Person.class))).thenReturn(person);

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
    public void shouldThrowAMultipleMainAddressExceptionWhenCreateAPersonWithMultipleMainAddress() {
        List<AddressAttachedPersonDTO> addressesDTO = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000001", true));
        CreatePersonDTO personDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(addressesDTO).build();

        assertThrows(MultipleMainAddressException.class, () -> personService.createPerson(personDTO));
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

    @Test
    public void shouldGetAPerson() {
        Person person = new Person(1L, "John Doe", LocalDate.parse("2000-01-01"), List.of());
        person.setAddresses(List.of(new Address(1L, "Rua da Lagoa", "10", "Recife", "PE", "0000000", true, person)));

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        personService.getPersonById(1L);

        verify(personRepository, times(1)).findById(1L);
    }

    @Test
    public void shouldNotFoundWhenGetAPerson() {
        assertEquals(personService.getPersonById(1L), Optional.empty());
        verify(personRepository, times(1)).findById(1L);
    }

    @Test
    public void shouldGetAllPeople() {
        Person firstPerson = new Person(1L, "John Doe", LocalDate.parse("2000-01-01"), List.of());
        firstPerson.setAddresses(List.of(new Address(1L, "Rua da Lagoa", "10", "Recife", "PE", "0000000", true, firstPerson)));
        Person secondPerson = new Person(1L, "John Doe", LocalDate.parse("2000-01-01"), List.of());
        secondPerson.setAddresses(List.of(new Address(1L, "Rua da Lagoa", "10", "Recife", "PE", "0000000", true, secondPerson)));
        List<Person> people = Arrays.asList(firstPerson, secondPerson);

        when(personRepository.findAllByOrderByIdAsc()).thenReturn(people);

        personService.getAllPeople();

        verify(personRepository, times(1)).findAllByOrderByIdAsc();
    }

    @Test
    public void shouldNotFoundWhenGetAllPeople() {
        assertEquals(personService.getAllPeople(), List.of());

        verify(personRepository, times(1)).findAllByOrderByIdAsc();
    }

    @Test
    public void shouldUpdateAPerson() throws MultipleMainAddressException, BusinessRuleException {
        List<AddressAttachedPersonDTO> addressesDTO = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000001", false));
        CreatePersonDTO createPersonDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(addressesDTO).build();

        Person person = new Person(1L, "John Doe", LocalDate.parse("2000-01-01"), List.of());
        person.setAddresses(List.of(new Address(1L, "Rua da Lagoa", "10", "Recife", "PE", "0000000", true, person)));

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        personService.updatePersonById(createPersonDTO, 1L);

        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void shouldNotFoundWhenUpdateAPerson() throws MultipleMainAddressException, BusinessRuleException {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertEquals(personService.updatePersonById(new CreatePersonDTO(), 1L), Optional.empty());
        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUpdateAPerson() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person()));

        assertThrows(IllegalArgumentException.class, () -> personService.updatePersonById(null, 1L));

        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void shouldThrowBusinessRuleExceptionWhenUpdateAPersonWithNullAddress() {
        CreatePersonDTO personDTO = CreatePersonDTO.builder()
            .name("John Doe")
            .birthDate(LocalDate.parse("2000-01-01")).build();
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person()));

        assertThrows(BusinessRuleException.class, () -> personService.updatePersonById(personDTO, 1L));

        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void shouldThrowBusinessRuleExceptionWhenUpdateAPersonWithEmptyAddress() {
        CreatePersonDTO personDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(List.of()).build();
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person()));

        assertThrows(BusinessRuleException.class, () -> personService.updatePersonById(personDTO, 1L));

        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void shouldThrowMultipleMainAddressExceptionWhenUpdateAPersonWithMultipleMainAddress() {
        List<AddressAttachedPersonDTO> addressesDTO = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000001", true));
        CreatePersonDTO personDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(addressesDTO).build();
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person()));

        assertThrows(MultipleMainAddressException.class, () -> personService.updatePersonById(personDTO, 1L));

        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void shouldThrowBusinessRuleExceptionWhenUpdateAPersonWithoutMainAddress() {
        List<AddressAttachedPersonDTO> addressesDTO = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", false),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000001", false));
        CreatePersonDTO personDTO = CreatePersonDTO.builder()
                .name("John Doe")
                .birthDate(LocalDate.parse("2000-01-01"))
                .addresses(addressesDTO).build();
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person()));

        assertThrows(BusinessRuleException.class, () -> personService.updatePersonById(personDTO, 1L));

        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(0)).save(any(Person.class));
    }

}