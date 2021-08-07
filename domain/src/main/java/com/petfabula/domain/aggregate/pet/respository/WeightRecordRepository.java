package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.WeightRecord;
import com.petfabula.domain.common.paging.CursorPage;

public interface WeightRecordRepository {

    WeightRecord save(WeightRecord weightRecord);

    WeightRecord findById(Long id);

    CursorPage<WeightRecord> findByPetId(Long petId, Long cursor, int size);

    void remove(WeightRecord weightRecord);
}
