package com.example.chat.services;

import com.example.chat.entities.Person;
import com.example.chat.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository  personRepository;

    public Person findById(int id) {
        try{
            System.out.println("Person with " + id + " was found in db");
            return personRepository.findById(id);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
