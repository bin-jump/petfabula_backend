package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.DisorderRecord;
import com.petfabula.domain.common.paging.CursorPage;

import java.time.Instant;

public interface DisorderRecordRepository {

    DisorderRecord save(DisorderRecord disorderRecord);

    DisorderRecord findById(Long id);

    DisorderRecord findByPetIdAndDateTime(Long petId, Instant dateTime);

    CursorPage<DisorderRecord> findByPetId(Long petId, Long cursor, int size);

    void remove(DisorderRecord disorderRecord);
}
