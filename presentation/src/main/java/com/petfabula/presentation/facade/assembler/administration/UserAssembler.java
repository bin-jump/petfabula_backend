package com.petfabula.presentation.facade.assembler.administration;

import com.petfabula.domain.aggregate.community.guardian.entity.Restriction;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.presentation.facade.dto.administration.UserWithStatusDto;
import com.petfabula.presentation.facade.dto.identity.UserAccountDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public UserWithStatusDto convertToDto(UserAccount userAccount, Restriction restriction) {
        UserWithStatusDto userWithStatusDto = modelMapper.map(userAccount, UserWithStatusDto.class);
        if (restriction != null && !restriction.expired()) {
            userWithStatusDto.setRestrictExpiration(restriction.getExpiration().toEpochMilli());
        }
        return userWithStatusDto;
    }
}
