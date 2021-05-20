package com.petfabula.domain.exception;

public class InvalidOperationException extends DomainBaseException {

    public InvalidOperationException(String message) {
        super(message);
    }
}