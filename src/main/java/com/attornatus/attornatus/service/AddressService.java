package com.attornatus.attornatus.service;

import com.attornatus.attornatus.dto.request.CreateAddressDTO;
import com.attornatus.attornatus.dto.response.ResponseAddressDTO;
import com.attornatus.attornatus.entity.Address;
import com.attornatus.attornatus.entity.Person;
import com.attornatus.attornatus.exception.BusinessRuleException;
import com.attornatus.attornatus.repository.AddressRepository;
import com.attornatus.attornatus.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;

    public AddressService(AddressRepository addressRepository, PersonRepository personRepository) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
    }

    private void addNewMainAddress(Person person, Address newMainAddress) {
        person.getAddresses().stream()
                .filter(Address::getMain)
                .forEach(address -> address.setMain(false));
        person.getAddresses().add(newMainAddress);
    }

    public ResponseAddressDTO createAddress(CreateAddressDTO addressDTO) throws BusinessRuleException {
        if (addressDTO == null) {
            throw new IllegalArgumentException("a address cannot be null");
        }

        Optional<Person> person = personRepository.findById(addressDTO.getPersonId());
        if (person.isEmpty()) {
            throw new BusinessRuleException("a person with this personId is not found");
        }

        Address newAddress = addressDTO.toEntity();
        newAddress.setPerson(person.get());
        boolean newMainAddress = newAddress.getMain();

        if (!newMainAddress) {
            person.get().getAddresses().add(newAddress);
            return addressRepository.save(newAddress).toResponseDto();
        }

        addNewMainAddress(person.get(), newAddress);
        return addressRepository.save(newAddress).toResponseDto();
    }
}
