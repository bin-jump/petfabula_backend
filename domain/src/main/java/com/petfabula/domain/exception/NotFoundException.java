package com.petfabula.domain.exception;

public class NotFoundException extends DomainBaseException {

    public NotFoundException(Object key) {
        super(key.toString());
    }

    public NotFoundException(String entityName, Object key) {
        super(key.toString());
    }

}
