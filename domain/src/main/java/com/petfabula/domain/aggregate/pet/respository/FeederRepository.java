package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.Feeder;

public interface FeederRepository {

    Feeder save(Feeder feeder);

    Feeder findById(Long id);
}
