package com.petfabula.infrastructure.persistence.jpa.identity.impl;

import com.petfabula.domain.aggregate.identity.entity.Prefecture;
import com.petfabula.domain.aggregate.identity.repository.PrefectureRepository;
import com.petfabula.infrastructure.persistence.jpa.identity.repository.PrefectureJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PrefectureRepositoryImpl implements PrefectureRepository {

    @Autowired
    private PrefectureJpaRepository prefectureJpaRepository;

    @Override
    public Prefecture findById(Long id) {
        return prefectureJpaRepository.findById(id).orElse(null);
    }

    @Override
    public Prefecture findByName(String name) {
        return prefectureJpaRepository.findByName(name);
    }

    @Override
    public List<Prefecture> findAll() {
        return prefectureJpaRepository.findAll();
    }

    @Override
    public Prefecture save(Prefecture prefecture) {
        return prefectureJpaRepository.save(prefecture);
    }
}
