package com.petfabula.domain.aggregate.pet.respository;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RecordCursor {

    private Instant dateTime;

    private Long id;

    public static RecordCursor of(Instant dateTime, Long id) {
        RecordCursor recordCursor = new RecordCursor(dateTime, id);
        return recordCursor;
    }
}
