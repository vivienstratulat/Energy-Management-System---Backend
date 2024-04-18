package com.sd.emsperson.services;

import com.sd.emsperson.dtos.KeycloakUserDto;
import com.sd.emsperson.dtos.PersonDTO;
import com.sd.emsperson.dtos.CreatePersonRegister;
import com.sd.emsperson.entities.Person;
import com.sd.emsperson.exceptions.AccountNotCreatedException;
import com.sd.emsperson.exceptions.ResourceNotRetrievedException;
import com.sd.emsperson.external.device.ExternalDeviceService;
import com.sd.emsperson.external.keycloak.ExternalKeycloakService;
import com.sd.emsperson.repositories.PersonRepository;
import com.sd.emsperson.services.mappers.PersonMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.CredentialException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final ExternalDeviceService externalDeviceService;
    private final ExternalKeycloakService externalKeycloakService;
    private final PersonMapper personMapper= Mappers.getMapper(PersonMapper.class);

    public List<PersonDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findPersonById(int id) {
        Optional<Person> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotRetrievedException(Person.class.getSimpleName() + " with id: " + id);
        }
        System.out.println("Person with " + id + " was found in db");
        return personMapper.toDTO(prosumerOptional.get());
    }

    public PersonDTO findPersonByExternalId(UUID id) {
        Optional<Person> prosumerOptional = personRepository.findByExternalId(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with external id {} was not found in db", id);
            throw new ResourceNotRetrievedException(Person.class.getSimpleName() + " with id: " + id);
        }
        System.out.println("Person with external" + id + " was found in db");
        return personMapper.toDTO(prosumerOptional.get());
    }

    @Transactional
    public int insert(CreatePersonRegister personRegister) {

        Optional<Person> existingUser = personRepository.findByEmail(personRegister.getEmail());
        if (existingUser.isPresent()) {
            LOGGER.error("Email {} is already in use", personRegister.getEmail());
            throw new ResourceNotRetrievedException(Person.class.getSimpleName() + " with email: " + personRegister.getEmail());
        }

        Person person = personMapper.toEntity(personRegister);
        //person.setExternalId(UUID.randomUUID());

        externalKeycloakService.saveUser(personRegister);

        KeycloakUserDto keycloakUserDto = externalKeycloakService.getAllUserDetails().stream()
                .filter(user -> user.getEmail().equals(personRegister.getEmail()))
                .findFirst()
                .orElseThrow(() -> new AccountNotCreatedException("Account not created"));

        person.setExternalId(keycloakUserDto.getId());

        person = personRepository.save(person);
        LOGGER.debug("Person with id {} was inserted in db", person.getId());
        System.out.println("Person with " + person.getId() + " was inserted in db");
        return person.getId();
    }

    public int update(String email, PersonDTO personDTO) {
        Optional<Person> personOptional = personRepository.findByEmail(email);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with email {} was not found in db", email);
            throw new ResourceNotRetrievedException(Person.class.getSimpleName() + " with email: " + email);
        }

        Person existingPerson = personOptional.get();
        existingPerson.setName(personDTO.getName());
        //existingPerson.setPassword(personDTO.getPassword());
        //  existingPerson.setRole(personDTO.getRole());
        existingPerson.setEmail(personDTO.getEmail());
        //  existingPerson.setExternalId(personDTO.getExternalId());

        existingPerson = personRepository.save(existingPerson);

        LOGGER.debug("Person with id {} was updated in db", existingPerson.getId());
        System.out.println("Person with " + existingPerson.getId() + " was updated in db");

        return existingPerson.getId();
    }

    @Transactional
    public String delete(String email) {
        Optional<Person> personOptional = personRepository.findByEmail(email);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with email {} was not found in db", email);
            throw new ResourceNotRetrievedException(Person.class.getSimpleName() + " with email: " + email);
        }
        Person person = personOptional.get();
        personRepository.deleteByEmail(email);
        externalKeycloakService.deleteUser(person.getExternalId());

        LOGGER.debug("Person with email {} was deleted from the db", email);
        System.out.println("Person with " + email + " was deleted from the db");


        externalDeviceService.deleteDevice(person.getExternalId());

        return email;
    }

//    public Person login(String email, String password) throws CredentialException {
//        Optional<Person> u = personRepository.findByEmailAndPassword(email, password);
//        if (u.isPresent()) {
//            System.out.println("Person with email " + u.get().getEmail() + " was logged in");
//            return u.get();
//
//        } else
//            throw new CredentialException("Wrong credentials");
//    }

    public Person findPersonByEmail(String email) {
        Optional<Person> prosumerOptional = personRepository.findByEmail(email);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with email {} was not found in db", email);
            throw new ResourceNotRetrievedException(Person.class.getSimpleName() + " with email: " + email);
        }
        System.out.println("Person with " + email + " was found in db");
        return prosumerOptional.get();
    }

    public int deletePersonByEmail(String email) {
        Optional<Person> personOptional = personRepository.findByEmail(email);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with email {} was not found in db", email);
            throw new ResourceNotRetrievedException(Person.class.getSimpleName() + " with email: " + email);
        }
        personRepository.deleteByEmail(email);
        LOGGER.debug("Person with email {} was deleted from the db", email);
        System.out.println("Person with " + email + " was deleted from the db");
        return personOptional.get().getId();
    }

    public UUID addDevice(PersonDTO personDTO) {
        UUID id = personDTO.getExternalId();
        ResponseEntity<UUID> response = new RestTemplate().getForEntity("http://localhost:8081/device/register/{id}", UUID.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Device with id " + id + " was added to person with id " + id);
            return id;
        } else
            return null;
    }

    public PersonDTO findById(int personId) {
        try {
            System.out.println("Person with " + personId + " was found in db");
            return personMapper.toDTO(personRepository.findById(personId).get());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Person> getAllAdmins() {
        List<Person> personList = personRepository.findAll();
        for(Person person: personList){
            System.out.println("astia s adminii"+person);
        }
        return personList.stream()
                .filter(person -> person.getRole().equals("admin"))
                .collect(Collectors.toList());
    }
}