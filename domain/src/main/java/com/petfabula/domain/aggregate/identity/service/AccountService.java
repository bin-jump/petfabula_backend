package com.petfabula.domain.aggregate.identity.service;

import com.petfabula.domain.aggregate.identity.MessageKey;
import com.petfabula.domain.aggregate.identity.entity.City;
import com.petfabula.domain.aggregate.identity.entity.Role;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.CityRepository;
import com.petfabula.domain.aggregate.identity.repository.RoleRepository;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.image.ImageRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private IdentityIdGenerator idGenerator;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ImageRepository imageRepository;

    public UserAccount createUser(String userName, String email, UserAccount.RegisterEntry registerEntry) {
//        UserAccount userAccount = userAccountRepository.findByName(userName);
//        if (userAccount != null) {
//            throw new InvalidValueException("name", MessageKey.USER_NAME_ALREADY_EXISTS);
//        }
//
//        Long accountId = idGenerator.nextId();
//        userAccount = new UserAccount(accountId, userName, registerEntry);
//        return userAccountRepository.save(userAccount);

        return createUser(userName, email, registerEntry, new ArrayList<>());
    }

    public UserAccount createUser(String userName, String email, UserAccount.RegisterEntry registerEntry, List<String> roleNames) {
//        UserAccount userAccount = userAccountRepository.findByName(userName);
//        if (userAccount != null) {
//            throw new InvalidValueException("name", MessageKey.USER_NAME_ALREADY_EXISTS);
//        }
        userName = validName(userName, registerEntry);

        UserAccount userAccount = userAccountRepository.findByEmail(email);
        if (userAccount != null) {
            throw new InvalidValueException("email", MessageKey.EMAIL_ALREADY_REGISTERED);
        }

        Long accountId = idGenerator.nextId();
        userAccount = new UserAccount(accountId, userName, email, registerEntry);

        for(String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName);
            if (role == null) {
                throw new InvalidValueException("roles", MessageKey.ROLE_NOT_EXISTS);
            }
            userAccount.addRole(role);
        }

        return userAccountRepository.save(userAccount);
    }

    public UserAccount update(Long accountId, LocalDate birthday, UserAccount.Gender gender,
                              String bio, Long cityId, ImageFile imageFile) {
        UserAccount userAccount = userAccountRepository.findById(accountId);
        if (userAccount == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        if (cityId != null) {
            City city = cityRepository.findById(cityId);
            if (city == null) {
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            }
        }

        userAccount.setBio(bio);
        userAccount.setBirthday(birthday);
        userAccount.setGender(gender);
        userAccount.setCityId(cityId);

        String photo = userAccount.getPhoto();
        if (imageFile != null) {
            photo = imageRepository.save(imageFile);
        }
        userAccount.setPhoto(photo);

        return userAccountRepository.save(userAccount);
    }

    private String validName(String name, UserAccount.RegisterEntry registerEntry) {
        // for apple rare case name could be null
        if (name == null && registerEntry == UserAccount.RegisterEntry.THIRD_PARTY) {
            return randomUserName();
        }

        UserAccount userAccount = userAccountRepository.findByName(name);
        if (userAccount != null) {
            // if oauth, try random suffix
            if (registerEntry == UserAccount.RegisterEntry.THIRD_PARTY) {
                for(int i = 0; i < 3; i++) {
                    String randName = randomSuffixName(name);
                    userAccount = userAccountRepository.findByName(randName);
                    if (userAccount == null) {
                        return randName;
                    }
                }
                // if all tries failed, just assert operation failure
                throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
            } else {
                // just assert field error if no try
                throw new InvalidValueException("name", MessageKey.USER_NAME_ALREADY_EXISTS);
            }
        }

        return name;
    }

    // max name length is 20
    private String randomSuffixName(String name) {
        name = name.substring(0, 17);
        return name + RandomStringUtils.randomNumeric(4);
    }

    private String randomUserName() {
        return "user" + RandomStringUtils.randomNumeric(6);
    }

}
