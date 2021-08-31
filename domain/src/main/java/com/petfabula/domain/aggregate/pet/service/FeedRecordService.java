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

    public FeedRecord create(Long feederId, Long petId, Instant dateTime, String foodContent, Integer amount, String note) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Long id = idGenerator.nextId();
        FeedRecord record = new FeedRecord(id, petId, dateTime, foodContent, amount, note);

        pet.setFeedRecordCount(pet.getFeedRecordCount() + 1);
        petRepository.save(pet);

        return feedRecordRepository.save(record);
    }

    public FeedRecord update(Long feederId, Long petId, Long recordId, Instant dateTime, String foodContent,
                             Integer amount, String note) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        FeedRecord record = feedRecordRepository.findById(recordId);
        if (record == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        if (!record.getPetId().equals(petId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        record.setDateTime(dateTime);
        record.setFoodContent(foodContent);
        record.setAmount(amount);
        record.setNote(note);

        return feedRecordRepository.save(record);
    }

    public FeedRecord remove(Long feederId, Long recordId) {
        FeedRecord record = feedRecordRepository.findById(recordId);
        if (record == null) {
            return null;
        }

        Pet pet = petRepository.findById(record.getPetId());
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        pet.setFeedRecordCount(pet.getFeedRecordCount() - 1);
        petRepository.save(pet);

        feedRecordRepository.remove(record);
        return record;
    }
}
