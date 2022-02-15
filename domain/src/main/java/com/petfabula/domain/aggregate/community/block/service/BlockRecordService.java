package com.petfabula.domain.aggregate.community.block.service;

import com.petfabula.domain.aggregate.community.block.ParticipatorBlockedDomainEvent;
import com.petfabula.domain.aggregate.community.block.entity.BlockRecord;
import com.petfabula.domain.aggregate.community.block.repository.BlockRecordRepository;
import com.petfabula.domain.aggregate.community.post.service.PostIdGenerator;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.DomainEventPublisher;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlockRecordService {

    @Autowired
    private BlockRecordRepository blockRecordRepository;

    @Autowired
    private PostIdGenerator idGenerator;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public BlockRecord block(Long participatorId, Long targetId) {
        if (participatorId.equals(targetId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        BlockRecord blockRecord = blockRecordRepository.find(participatorId, targetId);
        if (blockRecord != null) {
            return blockRecord;
        }

        Long id = idGenerator.nextId();
        blockRecord = new BlockRecord(id, participatorId, targetId);

        blockRecord = blockRecordRepository.save(blockRecord);

        domainEventPublisher.publish(new ParticipatorBlockedDomainEvent(participatorId, targetId));
        return blockRecord;
    }

    public BlockRecord removeBlock(Long participatorId, Long targetId) {
        BlockRecord blockRecord = blockRecordRepository.find(participatorId, targetId);
        if (blockRecord == null) {
            return blockRecord;
        }

        blockRecordRepository.remove(blockRecord);
        return blockRecord;
    }

    public boolean checkBlock(Long participatorId, Long targetId) {
        BlockRecord blockRecord = blockRecordRepository.find(participatorId, targetId);
        return blockRecord != null;
    }

    public void guardBlock(Long participatorId, Long targetId) {
        if (checkBlock(participatorId, targetId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
    }
}
