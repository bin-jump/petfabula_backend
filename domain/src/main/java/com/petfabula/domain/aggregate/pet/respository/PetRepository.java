package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.Pet;

import java.util.List;

public interface PetRepository {

    Pet save(Pet pet);

    Pet findById(Long id);

    Pet findByFeederIdAndName(Long feederId, String name);

    List<Pet> findByFeederId(Long id);

    void remove(Pet pet);
}
