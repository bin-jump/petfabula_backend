package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.common.paging.JumpableOffsetPage;

public interface UserAccountRepository {

    UserAccount findById(Long id);

    UserAccount findByName(String name);

    UserAccount findByEmail(String email);

    UserAccount save(UserAccount userAccount);

    JumpableOffsetPage<UserAccount> findAll(int pageIndex, int pageSize);
}
