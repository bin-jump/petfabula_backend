package com.petfabula.presentation.facade.dto.identity;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountDetailDto {

    private Long id;

    private String name;

    private String photo;

    private Long birthday;

    private UserAccount.Gender gender;

    private String bio;

    private Long cityId;

    private CityDto city;
}
