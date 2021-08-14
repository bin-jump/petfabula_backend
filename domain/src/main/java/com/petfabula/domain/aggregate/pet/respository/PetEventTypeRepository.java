package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.PetEventType;

import java.util.List;

public interface PetEventTypeRepository {

    PetEventType save(PetEventType petEventType);

    List<PetEventType> findAll();

    PetEventType findByEventType(String eventType);
}
