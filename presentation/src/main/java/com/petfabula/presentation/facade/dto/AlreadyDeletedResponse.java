package com.petfabula.presentation.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlreadyDeletedResponse {

    public static AlreadyDeletedResponse of(Long id) {
        return new AlreadyDeletedResponse(id);
    }

    private Long id;
}
