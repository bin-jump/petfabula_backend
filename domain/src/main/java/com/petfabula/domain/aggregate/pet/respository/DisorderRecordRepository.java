package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.DisorderRecord;
import com.petfabula.domain.common.paging.CursorPage;

public interface DisorderRecordRepository {

    DisorderRecord save(DisorderRecord disorderRecord);

    DisorderRecord findById(Long id);

    CursorPage<DisorderRecord> findByPetId(Long petId, Long cursor, int size);

    void remove(DisorderRecord disorderRecord);
}
