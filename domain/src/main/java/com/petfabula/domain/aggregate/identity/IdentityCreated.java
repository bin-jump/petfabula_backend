package com.petfabula.domain.aggregate.identity;

import com.petfabula.domain.common.domain.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdentityCreated extends DomainEvent {

    private Long id;

    private String name;

    private String email;
}
