package com.sd.emsperson.services.mappers;

import com.sd.emsperson.dtos.PersonDTO;
import com.sd.emsperson.dtos.CreatePersonRegister;
import com.sd.emsperson.entities.Person;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {
    Person toEntity(PersonDTO personDTO);
    PersonDTO toDTO(Person person);
    Person toEntity(CreatePersonRegister personRegister);
}
