package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.Prefecture;

import java.util.List;

public interface PrefectureRepository {

    Prefecture findById(Long id);

    Prefecture findByName(String name);

    List<Prefecture> findAll();

    Prefecture save(Prefecture prefecture);
}
