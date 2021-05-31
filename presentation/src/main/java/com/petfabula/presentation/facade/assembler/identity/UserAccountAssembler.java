package com.petfabula.presentation.facade.assembler.identity;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.identity.UserAccountDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAccountAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;

    public UserAccountDto convertToDto(UserAccount userAccount) {
        UserAccountDto userDto = modelMapper.map(userAccount, UserAccountDto.class);
        if (userAccount.getPhoto() != null) {
            userDto.setPhoto(assemblerHelper.completeImageUrl(userAccount.getPhoto()));
        }
        return userDto;
    }

}
