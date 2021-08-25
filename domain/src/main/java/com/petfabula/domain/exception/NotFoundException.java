package com.petfabula.domain.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends DomainBaseException {

    private Long entityId;

    public NotFoundException(Long id) {
        this.entityId = id;
    }

    public NotFoundException(Long id, String message) {
        super(message);
        this.entityId = id;
    }

}
