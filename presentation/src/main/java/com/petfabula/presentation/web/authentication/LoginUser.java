package com.petfabula.presentation.web.authentication;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.security.core.AuthenticatedPrincipal;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class LoginUser implements AuthenticatedPrincipal, Serializable {

    public static LoginUser newInstance(UserAccount userAccount) {
        LoginUser loginUser = LoginUser.builder()
                .userId(userAccount.getId())
                .build();
        return loginUser;
    }

    private Long userId;

    @Singular
    private List<String> privileges;

    @Override
    public String getName() {
        return userId.toString();
    }
}
