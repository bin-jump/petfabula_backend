package com.petfabula.domain.aggregate.community.participator;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipatorSearchItem {

    public static ParticipatorSearchItem of(Participator participator) {
        ParticipatorSearchItem user = ParticipatorSearchItem.builder()
                .id(participator.getId())
                .name(participator.getName())
                .photo(participator.getPhoto())
                .build();

        return user;
    }

    private Long id;

    private String name;

    private String photo;

}
