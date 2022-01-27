package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.common.paging.JumpableOffsetPage;

import java.util.List;

public interface UserAccountRepository {

    UserAccount findById(Long id);

    UserAccount findByName(String name);

    UserAccount findByEmail(String email);

    UserAccount save(UserAccount userAccount);

    List<UserAccount> findByIds(List<Long> ids);

    JumpableOffsetPage<UserAccount> findAll(int pageIndex, int pageSize);
}
