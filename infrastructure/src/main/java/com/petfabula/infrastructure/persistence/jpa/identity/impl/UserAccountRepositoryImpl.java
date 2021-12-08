package com.petfabula.infrastructure.persistence.jpa.identity.impl;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.identity.repository.UserAccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
