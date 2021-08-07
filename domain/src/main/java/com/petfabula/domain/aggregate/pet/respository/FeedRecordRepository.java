package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.FeedRecord;
import com.petfabula.domain.common.paging.CursorPage;

public interface FeedRecordRepository {

    FeedRecord save(FeedRecord disorderRecord);

    FeedRecord findById(Long id);

    CursorPage<FeedRecord> findByPetId(Long petId, Long cursor, int size);

    void remove(FeedRecord disorderRecord);
}
