package com.petfabula.domain.aggregate.identity.service;

import com.petfabula.domain.aggregate.identity.entity.City;
import com.petfabula.domain.aggregate.identity.entity.Prefecture;
import com.petfabula.domain.aggregate.identity.repository.CityRepository;
import com.petfabula.domain.aggregate.identity.repository.PrefectureRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {

    @Autowired
    private PrefectureRepository prefectureRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private IdentityIdGenerator idGenerator;

    public City createCity(Long prefectureId, String name) {
        City city = cityRepository.findByPrefectureIdAndName(prefectureId, name);
        if (city != null) {
            throw new InvalidOperationException(CommonMessageKeys.NAME_ALREADY_EXISTS);
        }

        Prefecture prefecture = prefectureRepository.findById(prefectureId);
        if (prefecture == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_DEPEND_ENTITY);
        }

        city = new City(idGenerator.nextId(), name,
                prefecture.getName(), prefectureId);
        return cityRepository.save(city);
    }

    public City updateCity(Long cityId, String name) {
        City city = cityRepository.findById(cityId);
        if (city == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        city.setName(name);
        return cityRepository.save(city);
    }

}
