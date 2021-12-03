package com.petfabula.domain.aggregate.identity.service;

import com.petfabula.domain.aggregate.identity.IdentityCreated;
import com.petfabula.domain.aggregate.identity.MessageKey;
import com.petfabula.domain.aggregate.identity.entity.EmailCodeAuthentication;
import com.petfabula.domain.aggregate.identity.entity.EmailPasswordAuthentication;
import com.petfabula.domain.aggregate.identity.entity.OauthAuthentication;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.*;
import com.petfabula.domain.aggregate.identity.service.oauth.AppleAuthContent;
import com.petfabula.domain.aggregate.identity.service.oauth.AppleService;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthResponse;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthService;
import com.petfabula.domain.common.domain.DomainEventPublisher;
import com.petfabula.domain.exception.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RegisterService {

    @Autowired
    private OauthService oauthService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private OauthAuthenticationRepository oauthAuthenticationRepository;

    @Autowired
    private EmailPasswordAuthenticationRepository emailPasswordAuthenticationRepository;

    @Autowired
    private EmailCodeAuthenticationRepository emailCodeAuthenticationRepository;

    @Autowired
    private AppleService appleService;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

//    public UserAccount registerByEmailPassword(String name, String email, String password, String verificationCode) {
//        verificationCodeService.checkEmailPasswordRegisterCode(email, verificationCode);
//
//        EmailPasswordAuthentication emailPasswordAuthentication =
//                emailPasswordAuthenticationRepository.findByEmail(email);
//        if (emailPasswordAuthentication != null) {
//            throw new InvalidValueException("email", MessageKey.EMAIL_ALREADY_REGISTERED);
//        }
//
//        UserAccount userAccount = accountService.createUser(name, UserAccount.RegisterEntry.EMAIL_PASSWORD);
//        emailPasswordAuthentication =
//                new EmailPasswordAuthentication(userAccount.getId(), email, password);
//
//        emailPasswordAuthenticationRepository.save(emailPasswordAuthentication);
//
//        return userAccount;
//    }

    public UserAccount createByEmailPasswordUntrustedEmailWithRoles(String name, String email, String password, List<String> roleNames) {
        EmailPasswordAuthentication emailPasswordAuthentication =
                emailPasswordAuthenticationRepository.findByEmail(email);
        if (emailPasswordAuthentication != null) {
            throw new InvalidValueException("email", MessageKey.EMAIL_ALREADY_REGISTERED);
        }
        UserAccount userAccount = accountService.createUser(name, UserAccount.RegisterEntry.OTHER, roleNames);
        emailPasswordAuthentication =
                new EmailPasswordAuthentication(userAccount.getId(), email, password);

        emailPasswordAuthenticationRepository.save(emailPasswordAuthentication);
        return userAccount;
    }

    public UserAccount registerByEmailCode(String name, String email, String code) {
        verificationCodeService.checkEmailCodeRegisterCode(email, code);

        EmailCodeAuthentication emailCodeAuthentication =
                emailCodeAuthenticationRepository.findByEmail(email);
        if (emailCodeAuthentication != null) {
            throw new InvalidValueException("email", MessageKey.EMAIL_ALREADY_REGISTERED);
        }

        UserAccount userAccount = userAccountRepository.findByName(name);
        if (userAccount != null) {
            throw new InvalidValueException("name", MessageKey.USER_NAME_ALREADY_EXISTS);
        }

        userAccount =
                accountService.createUser(name, UserAccount.RegisterEntry.EMAIL_CODE);
        emailCodeAuthentication =
                new EmailCodeAuthentication(userAccount.getId(), email);
        emailCodeAuthenticationRepository.save(emailCodeAuthentication);

        domainEventPublisher.publish(
                new IdentityCreated(userAccount.getId(), userAccount.getName(), userAccount.getEmail()));
        return userAccount;
    }

    public UserAccount registerOrAuthenticateByOauth(String serverName, String code) {
        OauthResponse response = oauthService.doOauth(serverName, code);
        OauthAuthentication oauthAuthentication = oauthAuthenticationRepository
                .findServerNameAndOauthId(serverName, response.getOauthId());

        if (oauthAuthentication == null) {
            UserAccount userAccount =
                    accountService.createUser(response.getUserName(), UserAccount.RegisterEntry.OAUTH);
            oauthAuthentication =
                    new OauthAuthentication(userAccount.getId(), serverName, response.getOauthId());

            oauthAuthenticationRepository.save(oauthAuthentication);

            domainEventPublisher.publish(
                    new IdentityCreated(userAccount.getId(), userAccount.getName(), userAccount.getEmail()));

            return userAccount;
        }
        return userAccountRepository.findById(oauthAuthentication.getId());
    }


    // if user already registered just login
    public UserAccount registerByAppleLogin(String name, String jwtToken) {
        AppleAuthContent authContent = appleService.validContentFromJwt(jwtToken);

        OauthAuthentication oauthAuthentication = oauthAuthenticationRepository
                .findServerNameAndOauthId(AppleService.SERVER_NAME, authContent.getUserId());

        if (oauthAuthentication == null) {
            UserAccount userAccount =
                    accountService.createUser(name, UserAccount.RegisterEntry.OAUTH);
            oauthAuthentication =
                    new OauthAuthentication(userAccount.getId(), AppleService.SERVER_NAME, authContent.getUserId());
            oauthAuthenticationRepository.save(oauthAuthentication);

            domainEventPublisher.publish(
                    new IdentityCreated(userAccount.getId(), userAccount.getName(), userAccount.getEmail()));
        }

        return userAccountRepository.findById(oauthAuthentication.getId());
    }

}
