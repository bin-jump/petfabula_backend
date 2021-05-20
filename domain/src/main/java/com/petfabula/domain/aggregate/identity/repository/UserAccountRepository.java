package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;

public interface UserAccountRepository {

    UserAccount findById(Long id);

    UserAccount findByName(String name);

    UserAccount save(UserAccount userAccount);
}
