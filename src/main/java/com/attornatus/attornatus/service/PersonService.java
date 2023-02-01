package com.attornatus.attornatus.service;

import com.attornatus.attornatus.dto.request.CreatePersonDTO;
import com.attornatus.attornatus.dto.response.ResponsePersonDTO;
import com.attornatus.attornatus.entity.Address;
import com.attornatus.attornatus.entity.Person;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.exception.MultipleMainAddressException;
import com.attornatus.attornatus.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public ResponsePersonDTO createPerson(CreatePersonDTO personDTO) throws MultipleMainAddressException, BusinessRuleException {
        if (personDTO == null) {
            throw new IllegalArgumentException("a person cannot be null");
        }
        if (personDTO.getAddresses() == null || personDTO.getAddresses().isEmpty()) {
            throw new BusinessRuleException("a person must have at least one address");
        }

        Person person = personDTO.toEntity();

        boolean alreadyHasMain = false;
        for (Address address : person.getAddresses()) {
            if (address.getMain()) {
                if (alreadyHasMain) {
                    throw new MultipleMainAddressException("a person cannot have multiple main addresses");
                }
                alreadyHasMain = true;
            }
        }
        if (!alreadyHasMain) {
            throw new BusinessRuleException("a person need to have a main address");
        }
        return personRepository.save(person).toDto();
    }

    public Optional<ResponsePersonDTO> getPersonById(final Long id) {
        Optional<Person> searchedPerson = personRepository.findById(id);
        if (searchedPerson.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(searchedPerson.get().toDto());
    }

    public List<ResponsePersonDTO> getAllPeople() {
        List<Person> people = personRepository.findAllByOrderByIdAsc();
        if (people.isEmpty()) {
            return Collections.emptyList();
        }
        List<ResponsePersonDTO> responsePeople = new ArrayList<>();
        for (Person person : people) {
            ResponsePersonDTO personDTO = person.toDto();
            responsePeople.add(personDTO);
        }
        return responsePeople;
    }

    public Optional<ResponsePersonDTO> updatePersonById(CreatePersonDTO personDTO, Long id) throws BusinessRuleException, MultipleMainAddressException {
        Optional<Person> personToUpdate = personRepository.findById(id);
        if (personToUpdate.isEmpty()) {
            return Optional.empty();
        }
        if (personDTO == null) {
            throw new IllegalArgumentException("a person cannot be null");
        }
        if (personDTO.getAddresses() == null || personDTO.getAddresses().isEmpty()) {
            throw new BusinessRuleException("a person must have at least one address");
        }

        Person person = personDTO.toEntity();
        person.setId(personToUpdate.get().getId());

        boolean alreadyHasMain = false;
        for (Address address : person.getAddresses()) {
            if (address.getMain()) {
                if (alreadyHasMain) {
                    throw new MultipleMainAddressException("a person cannot have multiple main addresses");
                }
                alreadyHasMain = true;
            }
        }
        if (!alreadyHasMain) {
            throw new BusinessRuleException("a person need to have a main address");
        }

        personToUpdate = Optional.of(person);

        return Optional.ofNullable(personRepository.save(personToUpdate.get()).toDto());
    }
}