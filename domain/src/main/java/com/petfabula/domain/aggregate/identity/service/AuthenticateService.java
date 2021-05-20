package com.petfabula.domain.aggregate.identity.service;

import com.petfabula.domain.aggregate.identity.entity.EmailCodeAuthentication;
import com.petfabula.domain.aggregate.identity.entity.EmailPasswordAuthentication;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.EmailCodeAuthenticationRepository;
import com.petfabula.domain.aggregate.identity.repository.EmailPasswordAuthenticationRepository;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.domain.exception.DomainAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticateService {

//    @Autowired
//    private PasswordEncoderService passwordEncoderService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailPasswordAuthenticationRepository emailAuthenticationRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EmailCodeAuthenticationRepository emailCodeAuthenticationRepository;

//    public UserAccount authenticateByEmailPassword(String email, String password) {
//        EmailPasswordAuthentication emailPasswordAuthentication =
//                emailAuthenticationRepository.findByEmail(email);
//        if (emailPasswordAuthentication == null) {
//            throw new DomainAuthenticationException("email", MessageKey.WRONG_EMAIL_OR_PASSWORD);
//        }
//        if (!passwordEncoderService.check(emailPasswordAuthentication.getPassword(), password)) {
//            throw new DomainAuthenticationException("password", MessageKey.WRONG_EMAIL_OR_PASSWORD);
//        }
//
//        return userAccountRepository.findById(emailPasswordAuthentication.getId());
//    }

    public void generateAndNotifyEmailCodeLoginCode(String email) {
        EmailCodeAuthentication emailCodeAuthentication =
                emailCodeAuthenticationRepository.findByEmail(email);
        if (emailCodeAuthentication == null) {
            throw new DomainAuthenticationException("email", MessageKey.EMAIL_NOT_REGISTERED);
        }

        verificationCodeService.generateAndNotifyWithEmailCodeLogin(email);
    }

    public UserAccount authenticateByEmailCode(String email, String code) {
        verificationCodeService.checkEmailCodeRegisterCode(email, code);

        EmailPasswordAuthentication emailPasswordAuthentication =
                emailAuthenticationRepository.findByEmail(email);
        if (emailPasswordAuthentication == null) {
            throw new DomainAuthenticationException("email", MessageKey.WRONG_EMAIL_OR_PASSWORD);
        }

        return userAccountRepository.findById(emailPasswordAuthentication.getId());
    }

}
