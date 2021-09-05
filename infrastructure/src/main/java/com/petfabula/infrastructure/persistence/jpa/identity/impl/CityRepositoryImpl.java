package com.petfabula.infrastructure.persistence.jpa.identity.impl;

import com.petfabula.domain.aggregate.identity.entity.City;
import com.petfabula.domain.aggregate.identity.repository.CityRepository;
import com.petfabula.infrastructure.persistence.jpa.identity.repository.CityJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityRepositoryImpl implements CityRepository {

    @Autowired
    private CityJpaRepository cityJpaRepository;

    @Override
    public City findByName(String name) {
        return cityJpaRepository.findByName(name);
    }

    @Override
    public City findById(Long id) {
        return cityJpaRepository.findById(id).orElse(null);
    }

    @Override
    public List<City> findAll() {
        return cityJpaRepository.findAll();
    }

    @Override
    public List<City> findByPrefectureId(Long prefectureId) {
        return null;
    }

    @Override
    public City save(City city) {
        return cityJpaRepository.save(city);
    }
}
