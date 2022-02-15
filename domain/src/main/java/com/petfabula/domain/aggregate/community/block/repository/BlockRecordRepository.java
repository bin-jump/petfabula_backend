package com.petfabula.domain.aggregate.community.block.repository;

import com.petfabula.domain.aggregate.community.block.entity.BlockRecord;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.paging.CursorPage;

public interface BlockRecordRepository {

    BlockRecord save(BlockRecord blockRecord);

    BlockRecord find(Long participatorId, Long targetId);

    CursorPage<Participator> findByParticipatorId(Long participatorId, Long cursor, int size);

    void remove(BlockRecord blockRecord);
}
