package com.petfabula.domain.aggregate.pet.service;

import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.entity.WeightRecord;
import com.petfabula.domain.aggregate.pet.respository.PetRepository;
import com.petfabula.domain.aggregate.pet.respository.WeightRecordRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class WeightRecordService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    @Autowired
    private WeightRecordRepository weightRecordRepository;

    public WeightRecord create(Long feederId, Long petId, Instant date, Double weight) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Long id = idGenerator.nextId();
        WeightRecord record = new WeightRecord(id, petId, date, weight);

        pet.setWeightRecordCount(pet.getWeightRecordCount() + 1);
        petRepository.save(pet);

        return weightRecordRepository.save(record);
    }

}
