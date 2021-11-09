package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.WeightRecord;
import com.petfabula.domain.common.paging.CursorPage;

import java.time.Instant;
import java.util.List;

public interface WeightRecordRepository {

    WeightRecord save(WeightRecord weightRecord);

    WeightRecord findById(Long id);

    WeightRecord findByPetIdAndDateTime(Long petId, Instant dateTime);

    CursorPage<WeightRecord> findByPetId(Long petId, Long cursor, int size);

    void remove(WeightRecord weightRecord);
}
