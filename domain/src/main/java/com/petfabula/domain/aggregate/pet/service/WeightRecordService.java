package com.petfabula.domain.aggregate.pet.service;

import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.entity.WeightRecord;
import com.petfabula.domain.aggregate.pet.respository.PetRepository;
import com.petfabula.domain.aggregate.pet.respository.WeightRecordRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class WeightRecordService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    @Autowired
    private WeightRecordRepository weightRecordRepository;

    public WeightRecord create(Long feederId, Long petId, Instant dateTime, Integer weight) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        dateTime = removeTimeInfo(dateTime);
        WeightRecord record = weightRecordRepository.findByPetIdAndDateTime(petId, dateTime);
        if (record != null) {
            throw new InvalidOperationException(CommonMessageKeys.RECORD_ALREADY_EXISTS);
        }

        Long id = idGenerator.nextId();
        record = new WeightRecord(id, petId, dateTime, weight);

        pet.setWeightRecordCount(pet.getWeightRecordCount() + 1);
        petRepository.save(pet);

        return weightRecordRepository.save(record);
    }

    public WeightRecord update(Long feederId, Long petId, Long recordId, Instant dateTime, Integer weight) {
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        WeightRecord record = weightRecordRepository.findById(recordId);
        if (record == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        if (!record.getPetId().equals(petId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        dateTime = removeTimeInfo(dateTime);
        if (!dateTime.equals(record.getDateTime())) {
            WeightRecord sameTime = weightRecordRepository.findByPetIdAndDateTime(petId, dateTime);
            if (sameTime != null) {
                throw new InvalidOperationException(CommonMessageKeys.RECORD_ALREADY_EXISTS);
            }
        }

        record.setDateTime(dateTime);
        record.setWeight(weight);

        return weightRecordRepository.save(record);
    }

    public WeightRecord remove(Long feederId, Long recordId) {
        WeightRecord record = weightRecordRepository.findById(recordId);
        if (record == null) {
            return null;
        }

        Pet pet = petRepository.findById(record.getPetId());
        if (pet == null || !pet.getFeederId().equals(feederId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        pet.setWeightRecordCount(pet.getWeightRecordCount() - 1);
        petRepository.save(pet);

        weightRecordRepository.remove(record);
        return record;
    }

    private Instant removeTimeInfo(Instant instant) {
        long timestamp = instant.toEpochMilli();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.toInstant();
    }

}
