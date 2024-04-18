package com.sd.emsperson.dtos;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTO  {
    private String name;
    private String role;
    private UUID externalId;
    private String email;
}