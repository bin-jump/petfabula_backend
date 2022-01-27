package com.petfabula.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailCodeRecordChangeEvent {

    private Long userId;

    private boolean active;
}
