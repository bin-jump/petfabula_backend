package com.petfabula.presentation.web.security.authencate.emailcode;

import com.petfabula.application.identity.IdentityApplicationService;
import com.petfabula.domain.aggregate.identity.entity.Role;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.exception.DomainAuthenticationException;
import com.petfabula.domain.exception.InvalidValueException;
import com.petfabula.presentation.web.authentication.LoginUser;
import com.petfabula.presentation.web.security.authencate.AppAuthenticationException;
import com.petfabula.presentation.web.security.authencate.SuccessAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class EmailCodeRegisterAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private IdentityApplicationService identityApplicationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        EmailCodeRegisterAuthenticationToken token = (EmailCodeRegisterAuthenticationToken)authentication;

        try {
            UserAccount account = identityApplicationService
                    .registerByEmailCode(token.getName(),  (String) token.getPrincipal(), (String) token.getCredentials());

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Set<Role> roles = account.getRoles();
            roles.stream().forEach(item ->
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + item.getName())));

            Authentication passedToken = new SuccessAuthenticationToken(LoginUser.newInstance(account), null,
                    authorities);

            return passedToken;

        } catch (DomainAuthenticationException | InvalidValueException e) {
            throw new AppAuthenticationException(e);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(EmailCodeRegisterAuthenticationToken.class);
    }
}
