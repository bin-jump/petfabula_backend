package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.MedicalRecord;
import com.petfabula.domain.common.paging.CursorPage;

import java.time.Instant;

public interface MedicalRecordRepository {

    MedicalRecord save(MedicalRecord medicalRecord);

    MedicalRecord findById(Long id);

    MedicalRecord findByPetIdAndDateTime(Long petId, Instant dateTime);

    CursorPage<MedicalRecord> findByPetId(Long petId, Long cursor, int size);

    void remove(MedicalRecord medicalRecord);
}
