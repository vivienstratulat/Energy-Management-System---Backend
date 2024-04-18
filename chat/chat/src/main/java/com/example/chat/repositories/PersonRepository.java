package com.example.chat.repositories;

import com.example.chat.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    public Person findById(int id);
}
