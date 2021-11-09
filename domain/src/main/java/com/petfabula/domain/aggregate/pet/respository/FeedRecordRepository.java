package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.FeedRecord;
import com.petfabula.domain.common.paging.CursorPage;

import java.time.Instant;
import java.util.List;

public interface FeedRecordRepository {

    FeedRecord save(FeedRecord disorderRecord);

    FeedRecord findById(Long id);

    FeedRecord findByPetIdAndDateTime(Long petId, Instant dateTime);

    CursorPage<FeedRecord> findByPetId(Long petId, Long cursor, int size);

    List<FeedRecord> findByPetIdAndAfter(Long petId, Instant dateTime, int sizeLimit);

    void remove(FeedRecord disorderRecord);
}
