package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.City;

import java.util.List;

public interface CityRepository {

    City findByPrefectureIdAndName(Long prefectureId, String name);

    City findById(Long id);

    List<City> findAll();

    List<City> findByPrefectureId(Long prefectureId);

    City save(City city);

    void remove(City city);
}
