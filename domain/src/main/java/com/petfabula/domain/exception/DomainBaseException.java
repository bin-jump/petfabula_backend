package com.petfabula.domain.exception;

public abstract class DomainBaseException extends RuntimeException {

    public DomainBaseException() {}
    public DomainBaseException(String message) {
        super(message);
    }
}
