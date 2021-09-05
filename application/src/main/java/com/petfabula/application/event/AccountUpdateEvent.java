package com.petfabula.application.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class AccountUpdateEvent {

    private Long id;

    private String name;

    private String photo;

    private String gender;

    private String bio;

    private LocalDate birthday;

    private Long cityId;
}
