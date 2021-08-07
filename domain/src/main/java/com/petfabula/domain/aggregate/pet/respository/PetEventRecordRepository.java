package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.PetEventRecord;
import com.petfabula.domain.common.paging.CursorPage;

public interface PetEventRecordRepository {

    PetEventRecord save(PetEventRecord petEventRecord);

    PetEventRecord findById(Long id);

    CursorPage<PetEventRecord> findByPetId(Long petId, Long cursor, int size);

    void remove(PetEventRecord petEventRecord);
}
