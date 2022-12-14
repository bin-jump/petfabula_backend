package com.petfabula.presentation.web.security.authencate.oauth;

import com.petfabula.application.identity.IdentityApplicationService;
import com.petfabula.domain.aggregate.identity.entity.Role;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.presentation.web.authentication.LoginUser;
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
public class OauthLoginProvider implements AuthenticationProvider {

    @Autowired
    private IdentityApplicationService identityApplicationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OauthLoginToken token = (OauthLoginToken) authentication;
        UserAccount account = identityApplicationService
                .authenticateByOauth(token.getServerName(), (String)token.getPrincipal());

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<Role> roles = account.getRoles();
        roles.stream().forEach(item ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + item.getName())));

        Authentication passedToken = new SuccessAuthenticationToken(LoginUser.newInstance(account), null,
                authorities);

        return passedToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(OauthLoginToken.class);
    }
}
