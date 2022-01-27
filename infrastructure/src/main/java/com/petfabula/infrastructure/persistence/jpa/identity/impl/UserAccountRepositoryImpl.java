package com.petfabula.infrastructure.persistence.jpa.identity.impl;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.domain.common.paging.JumpableOffsetPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.identity.repository.UserAccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserAccountRepositoryImpl implements UserAccountRepository {

    @Autowired
    UserAccountJpaRepository userAccountJpaRepository;

    @Override
    public UserAccount findById(Long id) {
        return userAccountJpaRepository.findById(id).orElse(null);
    }

    @Override
    public UserAccount findByName(String name) {
        return userAccountJpaRepository.findByName(name);
    }

    @Override
    public UserAccount findByEmail(String email) {
        return userAccountJpaRepository.findByEmail(email);
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        return userAccountJpaRepository.save(userAccount);
    }

    @Override
    public List<UserAccount> findByIds(List<Long> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        return userAccountJpaRepository.findByIdInOrderByIdDesc(ids);
    }

    @Override
    public JumpableOffsetPage<UserAccount> findAll(int pageIndex, int pageSize) {
        Pageable sortedById = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
        Page<UserAccount> accountPage = userAccountJpaRepository.findAll(sortedById);

        int cnt = (int) accountPage.getTotalElements();

        return JumpableOffsetPage.of(accountPage.getContent(), pageIndex, pageSize, cnt);
    }
}
