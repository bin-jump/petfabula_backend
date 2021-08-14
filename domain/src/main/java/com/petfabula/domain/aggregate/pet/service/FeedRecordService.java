package com.petfabula.domain.aggregate.pet.service;

import com.petfabula.domain.aggregate.pet.entity.FeedRecord;
import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.respository.FeedRecordRepository;
import com.petfabula.domain.aggregate.pet.respository.PetRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class FeedRecordService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    @Autowired
    private FeedRecordRepository feedRecordRepository;

    public FeedRecord create(Long feederId, Long petId, Instant date, String foodContent, Integer amount, String note) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Long id = idGenerator.nextId();
        FeedRecord record = new FeedRecord(id, petId, date, foodContent, amount, note);

        pet.setFeedRecordCount(pet.getFeedRecordCount() + 1);
        petRepository.save(pet);

        return feedRecordRepository.save(record);
    }
}
