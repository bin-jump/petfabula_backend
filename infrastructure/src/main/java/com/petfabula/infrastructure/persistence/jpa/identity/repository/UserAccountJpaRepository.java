package com.petfabula.infrastructure.persistence.jpa.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserAccountJpaRepository extends JpaRepository<UserAccount, Long> {

    @EntityGraph(value = "userAccount.all")
    UserAccount findByName(String name);

    @EntityGraph(value = "userAccount.all")
    UserAccount findByEmail(String email);

    @EntityGraph(value = "userAccount.all")
    @Query("select u from UserAccount u where u.id = :id")
    Optional<UserAccount> findById(Long id);

    List<UserAccount> findByIdInOrderByIdDesc(List<Long> ids);
}
