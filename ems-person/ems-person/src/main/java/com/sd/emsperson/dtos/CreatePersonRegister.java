package com.sd.emsperson.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePersonRegister {
    private String name;
    private String password;
    private String role;
    private UUID externalId;
    private String email;
}
