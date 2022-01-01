package com.petfabula.presentation.facade.dto.administration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithStatusDto {

    private Long id;

    private String name;

    private String photo;

    private String bio;

    private Long restrictExpiration;

}
