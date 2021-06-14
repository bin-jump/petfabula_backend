package com.petfabula.domain.common.event;

import com.petfabula.domain.common.domain.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreated extends DomainEvent {

    private Long id;

    private String name;

    private String photo;

}
