package com.petfabula.application.service;

import com.petfabula.domain.aggregate.identity.entity.EmailCodeAuthentication;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.EmailCodeAuthenticationRepository;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.domain.aggregate.identity.service.AuthenticateService;
import com.petfabula.domain.aggregate.identity.service.MessageKey;
import com.petfabula.domain.aggregate.identity.service.RegisterService;
import com.petfabula.domain.aggregate.identity.service.VerificationCodeService;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import com.petfabula.domain.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class IdentityApplicationService {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AuthenticateService authenticateService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EmailCodeAuthenticationRepository emailCodeAuthenticationRepository;

//    public void registerEmailVerificationCodeNotify(String email) {
//        verificationCodeService.generateAndNotifyWithEmailRegister(email);
//    }

//    public UserAccount registerByEmailPassword(String name, String email, String password, String verificationCode) {
//        UserAccount userAccount =
//                registerService.registerByEmailPassword(name, email, password, verificationCode);
//
//        // only remove when register transaction is committed
//        // it is ok to keep the old, as new code will overwrite the old,
//        // until it is either consumed or overwritten
//        verificationCodeService.removeEmailPasswordRegisterCode(email);
//        return userAccount;
//    }



    public UserAccount registerByEmailCode(String name, String email, String code) {
        UserAccount userAccount =
                registerService.registerByEmailCode(name, email, code);

        // only remove when register transaction is committed
        // it is ok to keep the old, as new code will overwrite the old,
        // until it is either consumed or overwritten
        verificationCodeService.removeEmailCodeRegisterCode(email);
        return userAccount;
    }

    public void examineEmailCodeRegiterContentAndSendCode(String name, String email) {
        EntityValidationUtils.validUserName("name", name);
        UserAccount userAccount = userAccountRepository.findByName(name);
        if (userAccount != null) {
            throw new InvalidValueException("name", MessageKey.USER_NAME_ALREADY_EXISTS);
        }
        EntityValidationUtils.validEmail("email", email);
        EmailCodeAuthentication emailCodeAuthentication =
                emailCodeAuthenticationRepository.findByEmail(email);
        if (emailCodeAuthentication != null) {
            throw new InvalidValueException("email", MessageKey.EMAIL_ALREADY_REGISTERED);
        }

        verificationCodeService.generateAndNotifyWithEmailCodeRegister(email);
    }


    public void sendEmailCodeLoginCode(String email) {
        authenticateService.generateAndNotifyEmailCodeLoginCode(email);
    }

    @Transactional
    public UserAccount authenticateByEmailCode(String email, String code) {
        return authenticateService.authenticateByEmailCode(email, code);
    }

    @Transactional
    public UserAccount registerOrAuthenticateByOauth(String serverName, String code) {
        UserAccount userAccount =
                registerService.registerOrAuthenticateByOauth(serverName, code);
        return userAccount;
    }

}
