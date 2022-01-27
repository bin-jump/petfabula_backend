package com.petfabula.domain.aggregate.identity.service;

import com.petfabula.domain.aggregate.identity.IdentityCreated;
import com.petfabula.domain.aggregate.identity.MessageKey;
import com.petfabula.domain.aggregate.identity.entity.*;
import com.petfabula.domain.aggregate.identity.repository.*;
import com.petfabula.domain.aggregate.identity.service.oauth.AppleAuthContent;
import com.petfabula.domain.aggregate.identity.service.oauth.AppleService;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthResponse;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthService;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.DomainEventPublisher;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RegisterService {

    static final long STATIC_CODE_USER_LIMIT = 3;

    @Autowired
    private OauthService oauthService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ThirdPartyAuthenticationRepository thirdPartyAuthenticationRepository;

    @Autowired
    private EmailPasswordAuthenticationRepository emailPasswordAuthenticationRepository;

    @Autowired
    private EmailCodeAuthenticationRepository emailCodeAuthenticationRepository;

    @Autowired
    private EmailCodeRecordRepository emailCodeRecordRepository;

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
        UserAccount userAccount = accountService.createUser(name, email, UserAccount.RegisterEntry.OTHER, roleNames);
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
                accountService.createUser(name, email, UserAccount.RegisterEntry.EMAIL_CODE);
        emailCodeAuthentication =
                new EmailCodeAuthentication(userAccount.getId(), email);
        emailCodeAuthenticationRepository.save(emailCodeAuthentication);

        domainEventPublisher.publish(
                new IdentityCreated(userAccount.getId(), userAccount.getName(), userAccount.getEmail()));
        return userAccount;
    }

    public UserAccount registerOrAuthenticateByOauth(String serverName, String code) {
        OauthResponse response = oauthService.doOauth(serverName, code);
        ThirdPartyAuthentication thirdPartyAuthentication = thirdPartyAuthenticationRepository
                .findServerNameAndThirdPartyId(serverName, response.getOauthId());

        if (thirdPartyAuthentication == null) {
            UserAccount userAccount = accountService
                    .createUser(response.getUserName(), response.getEmail(), UserAccount.RegisterEntry.THIRD_PARTY);
            thirdPartyAuthentication =
                    new ThirdPartyAuthentication(userAccount.getId(), serverName, response.getOauthId());

            thirdPartyAuthenticationRepository.save(thirdPartyAuthentication);

            domainEventPublisher.publish(
                    new IdentityCreated(userAccount.getId(), userAccount.getName(), userAccount.getEmail()));

            return userAccount;
        }
        return userAccountRepository.findById(thirdPartyAuthentication.getId());
    }


    // if user already registered just login
    public UserAccount registerByAppleLogin(String name, String jwtToken) {
        AppleAuthContent authContent = appleService.validContentFromJwt(jwtToken);

        ThirdPartyAuthentication thirdPartyAuthentication = thirdPartyAuthenticationRepository
                .findServerNameAndThirdPartyId(AppleService.SERVER_NAME, authContent.getUserId());

        if (thirdPartyAuthentication == null) {
            UserAccount userAccount =
                    accountService.createUser(name, authContent.getEmail(), UserAccount.RegisterEntry.THIRD_PARTY);
            thirdPartyAuthentication =
                    new ThirdPartyAuthentication(userAccount.getId(), AppleService.SERVER_NAME, authContent.getUserId());
            thirdPartyAuthenticationRepository.save(thirdPartyAuthentication);

            domainEventPublisher.publish(
                    new IdentityCreated(userAccount.getId(), userAccount.getName(), userAccount.getEmail()));
        }

        return userAccountRepository.findById(thirdPartyAuthentication.getId());
    }

    public UserAccount registerByStaticEmailCode(String name, String email, String code) {

        EmailCodeAuthentication emailCodeAuthentication =
                emailCodeAuthenticationRepository.findByEmail(email);
        if (emailCodeAuthentication != null) {
            throw new InvalidValueException("email", MessageKey.EMAIL_ALREADY_REGISTERED);
        }

        UserAccount userAccount = userAccountRepository.findByName(name);
        if (userAccount != null) {
            throw new InvalidValueException("name", MessageKey.USER_NAME_ALREADY_EXISTS);
        }

        // not 100% guarantee, but it is ok here
        long recordCount = emailCodeRecordRepository.count();
        if (recordCount >= STATIC_CODE_USER_LIMIT) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        userAccount =
                accountService.createUser(name, email, UserAccount.RegisterEntry.OTHER);
        emailCodeAuthentication =
                new EmailCodeAuthentication(userAccount.getId(), email);
        emailCodeAuthenticationRepository.save(emailCodeAuthentication);

        EmailCodeRecord emailCodeRecord = new EmailCodeRecord(userAccount.getId(), email, code);
        emailCodeRecord = emailCodeRecordRepository.save(emailCodeRecord);

        domainEventPublisher.publish(
                new IdentityCreated(userAccount.getId(), userAccount.getName(), userAccount.getEmail()));
        return userAccount;
    }

    public EmailCodeRecord updateStaticEmailCodeRecord(String email, String code, boolean active) {
        EmailCodeRecord record = emailCodeRecordRepository.findByEmail(email);
        if (record == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        record.setCode(code);
        record = emailCodeRecordRepository.save(record);
        record.setActive(active);

        return record;
    }

}
