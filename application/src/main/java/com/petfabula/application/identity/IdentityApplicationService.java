package com.petfabula.application.identity;

import com.petfabula.application.event.AccountCreatedEvent;
import com.petfabula.application.event.AccountUpdateEvent;
import com.petfabula.application.event.IntegratedEventPublisher;
import com.petfabula.domain.aggregate.identity.MessageKey;
import com.petfabula.domain.aggregate.identity.entity.EmailCodeAuthentication;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.EmailCodeAuthenticationRepository;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.domain.aggregate.identity.service.*;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import com.petfabula.domain.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class IdentityApplicationService {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AuthenticateService authenticateService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EmailCodeAuthenticationRepository emailCodeAuthenticationRepository;

    @Autowired
    private IntegratedEventPublisher eventPublisher;

    public void examineEmailCodeRegisterContentAndSendCode(String name, String email) {
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


    @Transactional
    public UserAccount createByEmailPasswordUntrustedEmailWithRoles(String name, String email, String password, List<String> roleNames) {
        UserAccount userAccount =
                registerService.createByEmailPasswordUntrustedEmailWithRoles(name, email, password, roleNames);
        eventPublisher
                .publish(new AccountCreatedEvent(userAccount.getId(), userAccount.getName(), userAccount.getPhoto()));
        return userAccount;
    }

    @Transactional
    public UserAccount registerByEmailCode(String name, String email, String code) {
        UserAccount userAccount =
                registerService.registerByEmailCode(name, email, code);
        eventPublisher
                .publish(new AccountCreatedEvent(userAccount.getId(), userAccount.getName(), userAccount.getPhoto()));
        return userAccount;
    }

    @Transactional
    public UserAccount registerOrAuthenticateByOauth(String serverName, String code) {
        UserAccount userAccount =
                registerService.registerOrAuthenticateByOauth(serverName, code);
        eventPublisher
                .publish(new AccountCreatedEvent(userAccount.getId(), userAccount.getName(), userAccount.getPhoto()));
        return userAccount;
    }

    public void sendEmailCodeLoginCode(String email) {
        authenticateService.generateAndNotifyEmailCodeLoginCode(email);
    }

    @Transactional
    public UserAccount authenticateByEmailCode(String email, String code) {
        return authenticateService.authenticateByEmailCode(email, code);
    }

    @Transactional
    public UserAccount authenticateByEmailPassword(String email, String password) {
        return authenticateService.authenticateByEmailPassword(email, password);
    }

    @Transactional
    public UserAccount updateAccount(Long accountId, LocalDate birthday, UserAccount.Gender gender,
                                     String bio, Long cityId, ImageFile imageFile) {
        UserAccount account = accountService.update(accountId, birthday, gender,
                bio, cityId, imageFile);
        String genderStr = account.getGender() != null ? account.getGender().toString() : null;
        AccountUpdateEvent event = AccountUpdateEvent.builder()
                .id(accountId)
                .name(account.getName())
                .photo(account.getPhoto())
                .bio(account.getBio())
                .birthday(account.getBirthday())
                .gender(genderStr)
                .cityId(account.getCityId())
                .build();
        eventPublisher
                .publish(event);
        return account;
    }

}
