package com.sd.emsperson.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage entityNotFoundHandler(EntityNotFoundException ex, WebRequest request) {
        return new ErrorMessage(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler({ResourceNotRetrievedException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage resourceUnreachableException(ResourceNotRetrievedException ex, WebRequest request) {
        return new ErrorMessage(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler({ExternalServiceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage externalException(ExternalServiceException ex, WebRequest request) {
        return new ErrorMessage(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(AccountNotCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage cannotCreateAccountHandler(AccountNotCreatedException ex, WebRequest request) {
        return new ErrorMessage(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
    }
}
