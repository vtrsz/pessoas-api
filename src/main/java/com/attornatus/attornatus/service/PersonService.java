package com.attornatus.attornatus.service;

import com.attornatus.attornatus.entity.Address;
import com.attornatus.attornatus.entity.Person;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.exception.MultipleMainAddressException;
import com.attornatus.attornatus.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person createPerson(Person person) throws MultipleMainAddressException, BusinessRuleException {
        if (person.getAddresses() == null || person.getAddresses().isEmpty()) {
            throw new BusinessRuleException("a person must have at least one address");
        }
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
        personRepository.save(person);
        return person;
    }
}