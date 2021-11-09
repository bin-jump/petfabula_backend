package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.PetEventRecord;
import com.petfabula.domain.common.paging.CursorPage;

import java.time.Instant;

public interface PetEventRecordRepository {

    PetEventRecord save(PetEventRecord petEventRecord);

    PetEventRecord findById(Long id);

    PetEventRecord findByPetIdAndDateTime(Long petId, Instant dateTime);

    CursorPage<PetEventRecord> findByPetId(Long petId, Long cursor, int size);

    void remove(PetEventRecord petEventRecord);
}
