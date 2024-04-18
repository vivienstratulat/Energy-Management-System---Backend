package com.sd.emsperson.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRepresentationRequest {
    private String username;
    private Boolean enabled;
    private String email;
    private List<CredentialRepresentationRequest> credentials;
    private List<String> groups;



}
