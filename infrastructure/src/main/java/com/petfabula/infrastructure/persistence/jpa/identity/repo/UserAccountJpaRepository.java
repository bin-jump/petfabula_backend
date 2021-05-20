package com.petfabula.infrastructure.persistence.jpa.identity.repo;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountJpaRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByName(String name);
}
