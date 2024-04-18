package com.sd.emsperson.repositories;
import com.sd.emsperson.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    List<Person> findByName(String name);
    Optional<Person> findByEmail(String email);
    String deleteByEmail(String email);
    Optional<Person> findByExternalId(UUID externalId);
}
