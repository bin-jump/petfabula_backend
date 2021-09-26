package com.petfabula.presentation.web.security.authencate;

import com.petfabula.application.identity.IdentityApplicationService;
import com.petfabula.domain.aggregate.identity.entity.Role;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.exception.DomainAuthenticationException;
import com.petfabula.presentation.web.authentication.LoginUser;
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
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private IdentityApplicationService identityApplicationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        EmailCodeAuthenticationToken token = (EmailCodeAuthenticationToken)authentication;

        try {
            UserAccount account = identityApplicationService
                    .authenticateByEmailCode((String) token.getPrincipal(), (String) token.getCredentials());

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Set<Role> roles = account.getRoles();
            roles.stream().forEach(item ->
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + item.getName())));

            Authentication passedToken = new SuccessAuthenticationToken(LoginUser.newInstance(account), null,
                    authorities);

            return passedToken;

        } catch (DomainAuthenticationException e) {
            throw new AppAuthenticationException(e);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(EmailCodeAuthenticationToken.class);
    }
}
