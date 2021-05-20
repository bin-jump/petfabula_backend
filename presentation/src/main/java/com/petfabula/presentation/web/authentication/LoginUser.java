package com.petfabula.presentation.web.authentication;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class LoginUser {

    public static LoginUser newInstance(UserAccount userAccount) {
        LoginUser loginUser = LoginUser.builder()
                .userId(userAccount.getId())
                .build();
        return loginUser;
    }

    private Long userId;

    @Singular
    private List<String> privileges;
}
