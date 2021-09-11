package com.petfabula.infrastructure.persistence.jpa.identity.impl;

import com.petfabula.domain.aggregate.identity.entity.Role;
import com.petfabula.domain.aggregate.identity.repository.RoleRepository;
import com.petfabula.infrastructure.persistence.jpa.identity.repository.RoleJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Override
    public Role findByName(String name) {
        return roleJpaRepository.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return roleJpaRepository.save(role);
    }
}
