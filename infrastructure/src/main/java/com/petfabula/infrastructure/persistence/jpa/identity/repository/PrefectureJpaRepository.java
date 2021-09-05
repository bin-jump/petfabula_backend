package com.petfabula.infrastructure.persistence.jpa.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.Prefecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrefectureJpaRepository extends JpaRepository<Prefecture, Long> {

    Prefecture findByName(String name);
}
