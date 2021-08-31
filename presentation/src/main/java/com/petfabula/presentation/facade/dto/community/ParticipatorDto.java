package com.petfabula.presentation.facade.dto.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipatorDto {

    private Long id;

    private String name;

    private String photo;

    private String bio;

    private Integer postCount;

    private Integer questionCount;

    private Integer followerCount;

    private Integer followedCount;

    private Integer petCount;

    private boolean followed;
}
