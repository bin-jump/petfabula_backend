package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.Role;

public interface RoleRepository {

    Role findByName(String name);

    Role save(Role role);
}
