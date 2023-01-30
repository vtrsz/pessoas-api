package com.attornatus.attornatus.service;

import com.attornatus.attornatus.dto.request.AddressAttachedPersonDTO;
import com.attornatus.attornatus.entity.Address;
import com.attornatus.attornatus.entity.Person;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.exception.MultipleMainAddressException;
import com.attornatus.attornatus.repository.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @AfterEach
    void resetMocking() {
        Mockito.reset(personRepository);
    }

    @Test
    public void createPerson() throws MultipleMainAddressException, BusinessRuleException {
        Person person =  new Person(1L, "John Doe", LocalDate.parse("2000-01-01"), null);
        List<AddressAttachedPersonDTO> addressesDTO = Arrays.asList(
                new AddressAttachedPersonDTO("Rua da Escola", "1502", "São Paulo", "SP", "00000000", true),
                new AddressAttachedPersonDTO("Rua da Lagoa", "10", "Recife", "PE", "00000001", false));
        List<Address> addresses = addressesDTO.stream()
                .map(addressAttachedPersonDTO -> addressAttachedPersonDTO.toEntity(person))
                .collect(Collectors.toList());

        person.setAddresses(addresses);

        Mockito.when(personRepository.save(person)).thenReturn(person);

        personService.createPerson(person);

        verify(personRepository, times(1)).save(person);
    }
}