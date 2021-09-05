package com.petfabula.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountCreatedEvent {

    private Long id;

    private String name;

    private String photo;
}
