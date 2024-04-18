package com.sd.emsperson.external.keycloak;

import com.sd.emsperson.dtos.KeycloakUserDto;
import com.sd.emsperson.dtos.CreatePersonRegister;
import com.sd.emsperson.exceptions.AccountNotCreatedException;
import com.sd.emsperson.exceptions.ExternalServiceException;
import com.sd.emsperson.exceptions.ResourceNotRetrievedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalKeycloakService {
    private final KeycloakRestClient keycloakRestClient;

    public HttpStatus saveUser(CreatePersonRegister personRegister) {
        try {
            return keycloakRestClient.saveUser(personRegister);
        } catch (ExternalServiceException ex) {
            String errorMessage = "Error creating user: " + ex.getMessage();
            log.error(errorMessage);
            throw new AccountNotCreatedException(errorMessage);
        }
    }

    public KeycloakUserDto getUserDetails(UUID userExternalId) {
        try {
            return keycloakRestClient.getUserDetailsDto(userExternalId);
        } catch (ExternalServiceException ex) {
            String errorMessage = "Error retrieving user details " + ex.getMessage();
            log.error(errorMessage);
            throw new ResourceNotRetrievedException(errorMessage);
        }
    }

    public List<KeycloakUserDto> getAllUserDetails() {
        try {
            return keycloakRestClient.getAllUserDetails();
        } catch (ExternalServiceException ex) {
            String errorMessage = "Error retrieving all user details " + ex.getMessage();
            log.error(errorMessage);
            throw new ResourceNotRetrievedException(errorMessage);
        }
    }

    public HttpStatus deleteUser(UUID externalId) {
        try {
            return keycloakRestClient.deleteUser(externalId);
        } catch (ExternalServiceException ex) {
            String errorMessage = "Error deleting user " + ex.getMessage();
            log.error(errorMessage);
            throw new ResourceNotRetrievedException(errorMessage);
        }
    }

}
