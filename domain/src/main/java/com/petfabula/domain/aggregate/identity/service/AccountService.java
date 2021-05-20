package com.petfabula.domain.aggregate.identity.service;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.domain.exception.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.plugin2.message.Message;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private IdentityIdGenerator idGenerator;

    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccount createUser(String userName, UserAccount.RegisterEntry registerEntry) {
        UserAccount userAccount = userAccountRepository.findByName(userName);
        if (userAccount != null) {
            throw new InvalidValueException("name", MessageKey.USER_NAME_ALREADY_EXISTS);
        }

        Long accountId = idGenerator.nextId();
        userAccount = new UserAccount(accountId, userName, registerEntry);
        return userAccountRepository.save(userAccount);
    }
}
