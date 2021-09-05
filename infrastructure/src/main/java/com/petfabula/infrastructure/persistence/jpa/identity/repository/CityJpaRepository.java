package com.petfabula.infrastructure.persistence.jpa.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityJpaRepository extends JpaRepository<City, Long> {

    City findByName(String name);
}
