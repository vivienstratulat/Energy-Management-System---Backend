package com.sd.emsperson.controllers;

import com.sd.emsperson.dtos.PersonDTO;
import com.sd.emsperson.dtos.CreatePersonRegister;
import com.sd.emsperson.entities.Person;
import com.sd.emsperson.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/person")
//@PreAuthorize("hasAnyRole('admin_role','client_role')")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

   /* @GetMapping("/getAll")
    public ResponseEntity<List<PersonDTO>> getPersons() {

        List<PersonDTO> dtos = personService.findPersons();
        for (PersonDTO dto : dtos) {
            Link personLink = linkTo(methodOn(PersonController.class)
                    .getPerson(dto.getId())).withRel("personDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }*/

   // @PreAuthorize("hasRole('admin')")
    @GetMapping("/getAll")
    public ResponseEntity<List<PersonDTO>> getPersons() {

        List<PersonDTO> dtos = personService.findPersons();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Integer> insertProsumer(@RequestBody CreatePersonRegister personDTO) {
        int personID = personService.insert(personDTO);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/getByExternalId/{id}")
    public ResponseEntity<PersonDTO> getPersonByExternalId(@PathVariable("id") UUID personId) {
        PersonDTO dto = personService.findPersonByExternalId(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

  /*  @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody PersonDTO personDTO) throws CredentialException {
        //Person person = personService.login(personDTO.getEmail(),"");//personDTO.getPassword());
        return new ResponseEntity<>("person", HttpStatus.OK);
    }*/

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable("id") int personId) {
        PersonDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/getByEmail/{email}")
    public ResponseEntity<Person> getPersonByEmail(@PathVariable("email") String email) {
        Person person = personService.findPersonByEmail(email);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping("/update/{email}")
    ResponseEntity<Integer> updatePerson(@PathVariable("email") String email, @RequestBody PersonDTO personDTO) {
        int personID = personService.update(email,personDTO);
        return new ResponseEntity<>(personID, HttpStatus.OK);
    }

    @PostMapping("/delete/{email}")
    ResponseEntity<String> deletePerson(@PathVariable("email") String email) {
        String personEmail = personService.delete(email);
        return new ResponseEntity<>(personEmail, HttpStatus.OK);

    }

    @PostMapping("/addDevice/{id}")
    ResponseEntity<UUID> addDevice(@RequestBody PersonDTO personDTO, @PathVariable("id") UUID personId) {
        UUID personID = personService.addDevice(personDTO);
        return new ResponseEntity<>(personID, HttpStatus.OK);

    }

    @GetMapping(value = "/getById/{id}")
    ResponseEntity<PersonDTO> getPersonById(@PathVariable("id") int personId) {
        PersonDTO person = personService.findById(personId);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllAdmins")
    ResponseEntity<List<Person>> getAllAdmins() {
        System.out.println("getAllAdmins in controller");
        List<Person> admins = personService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }
    //TODO: UPDATE, DELETE per resource

}