package com.petfabula.domain.aggregate.community.participator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ParticipatorSearchItem {

    private Long id;

    private String name;

    private String photo;

}
