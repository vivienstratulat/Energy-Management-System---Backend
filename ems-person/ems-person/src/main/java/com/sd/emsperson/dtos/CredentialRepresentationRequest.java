package com.sd.emsperson.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CredentialRepresentationRequest {
    private String type;
    private String value;
    private boolean temporary;

}
