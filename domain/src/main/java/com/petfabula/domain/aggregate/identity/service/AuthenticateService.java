package com.petfabula.domain.aggregate.identity.service;

import com.petfabula.domain.aggregate.identity.MessageKey;
import com.petfabula.domain.aggregate.identity.entity.*;
import com.petfabula.domain.aggregate.identity.repository.*;
import com.petfabula.domain.aggregate.identity.service.oauth.AppleAuthContent;
import com.petfabula.domain.aggregate.identity.service.oauth.AppleService;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthResponse;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthService;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.DomainAuthenticationException;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticateService {

    @Autowired
    private PasswordEncoderService passwordEncoderService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailPasswordAuthenticationRepository emailAuthenticationRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EmailCodeAuthenticationRepository emailCodeAuthenticationRepository;

    @Autowired
    private ThirdPartyAuthenticationRepository thirdPartyAuthenticationRepository;

    @Autowired
    private EmailCodeRecordRepository emailCodeRecordRepository;

    @Autowired
    private AppleService appleService;

    @Autowired
    private OauthService oauthService;

    public UserAccount authenticateByEmailPassword(String email, String password) {
        EmailPasswordAuthentication emailPasswordAuthentication =
                emailAuthenticationRepository.findByEmail(email);
        if (emailPasswordAuthentication == null) {
            throw new DomainAuthenticationException("email", MessageKey.WRONG_EMAIL_OR_PASSWORD);
        }
        if (!passwordEncoderService.check(emailPasswordAuthentication.getPassword(), password)) {
            throw new DomainAuthenticationException("password", MessageKey.WRONG_EMAIL_OR_PASSWORD);
        }

        return userAccountRepository.findById(emailPasswordAuthentication.getId());
    }

    public void generateAndNotifyEmailCodeLoginCode(String email) {
        EmailCodeRecord record = emailCodeRecordRepository.findByEmail(email);
        // skip if email has a static code record
        if (record != null) {
            return;
        }

        EmailCodeAuthentication emailCodeAuthentication =
                emailCodeAuthenticationRepository.findByEmail(email);
        if (emailCodeAuthentication == null) {
            throw new DomainAuthenticationException("email", MessageKey.EMAIL_NOT_REGISTERED);
        }

        verificationCodeService.generateAndNotifyWithEmailCodeLogin(email);
    }

    public UserAccount authenticateByEmailCode(String email, String code) {
        verificationCodeService.checkEmailLoginCode(email, code);

        EmailCodeAuthentication emailCodeAuthentication =
                emailCodeAuthenticationRepository.findByEmail(email);
        if (emailCodeAuthentication == null) {
            throw new DomainAuthenticationException("email", MessageKey.EMAIL_NOT_REGISTERED);
        }

        verificationCodeService.removeEmailLoginCode(email);
        return userAccountRepository.findById(emailCodeAuthentication.getId());
    }

    public UserAccount authenticateByStaticEmailCode(String email, String code) {
        EmailCodeAuthentication emailCodeAuthentication =
                emailCodeAuthenticationRepository.findByEmail(email);
        if (emailCodeAuthentication == null) {
            throw new DomainAuthenticationException("email", MessageKey.EMAIL_NOT_REGISTERED);
        }

        EmailCodeRecord record = emailCodeRecordRepository.findByEmail(email);
        if (record == null ||
            !record.isValid() ||
            !record.getCode().equals(code)) {
            throw new DomainAuthenticationException("code", MessageKey.INVALID_VERIFICATION_CODE);
        }

        return userAccountRepository.findById(emailCodeAuthentication.getId());
    }

    public UserAccount authenticateByOauth(String serverName, String code) {
        OauthResponse response = oauthService.doOauth(serverName, code);
        ThirdPartyAuthentication thirdPartyAuthentication = thirdPartyAuthenticationRepository
                .findServerNameAndThirdPartyId(serverName, response.getOauthId());

        if (thirdPartyAuthentication == null) {
            throw new InvalidOperationException(MessageKey.NOT_REGISTERED);
        }

        return userAccountRepository.findById(thirdPartyAuthentication.getId());
    }

    public UserAccount authenticateByAppleLogin(String jwtToken) {
        AppleAuthContent authContent = appleService.validContentFromJwt(jwtToken);
        ThirdPartyAuthentication thirdPartyAuthentication = thirdPartyAuthenticationRepository
                .findServerNameAndThirdPartyId(AppleService.SERVER_NAME, authContent.getUserId());

        if (thirdPartyAuthentication == null) {
            throw new InvalidOperationException(MessageKey.NOT_REGISTERED);
        }

        return userAccountRepository.findById(thirdPartyAuthentication.getId());
    }

    public void changePassword(Long userId, String password, String newPassword) {
        EmailPasswordAuthentication emailPasswordAuthentication =
                emailAuthenticationRepository.findById(userId);
        if (emailPasswordAuthentication == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (!passwordEncoderService.check(emailPasswordAuthentication.getPassword(), password)) {
            throw new DomainAuthenticationException("password", MessageKey.WRONG_EMAIL_OR_PASSWORD);
        }

        try {
            emailPasswordAuthentication.setPassword(newPassword);
        } catch (InvalidValueException ex) {
            throw new InvalidValueException("newPassword", com.petfabula.domain.common.validation.MessageKey.INVALID_PASSWORD);
        }

        emailAuthenticationRepository.save(emailPasswordAuthentication);
    }
}
