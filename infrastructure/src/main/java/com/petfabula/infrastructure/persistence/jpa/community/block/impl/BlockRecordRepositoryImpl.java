package com.petfabula.infrastructure.persistence.jpa.community.block.impl;

import com.petfabula.domain.aggregate.community.block.entity.BlockRecord;
import com.petfabula.domain.aggregate.community.block.repository.BlockRecordRepository;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.community.block.repository.BlockRecordJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BlockRecordRepositoryImpl implements BlockRecordRepository {

    @Autowired
    private BlockRecordJpaRepository blockRecordJpaRepository;

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Override
    public BlockRecord save(BlockRecord blockRecord) {
        return blockRecordJpaRepository.save(blockRecord);
    }

    @Override
    public BlockRecord find(Long participatorId, Long targetId) {
        return blockRecordJpaRepository.findByParticipatorIdAndTargetId(participatorId, targetId);
    }

    @Override
    public CursorPage<Participator> findByParticipatorId(Long participatorId, Long cursor, int size) {

        Specification<BlockRecord> spec = new Specification<BlockRecord>() {
            @Override
            public Predicate toPredicate(Root<BlockRecord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("participatorId"), participatorId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("targetId"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<BlockRecord> blockRecords = blockRecordJpaRepository.findAll(spec, limit);

        Long nextCursor = null;
        if (blockRecords.hasNext()) {
            nextCursor = blockRecords.getContent()
                    .get(blockRecords.getContent().size() - 1).getId();
        }

        List<Long> ids = blockRecords.getContent().stream()
                .map(BlockRecord::getTargetId).collect(Collectors.toList());
        List<Participator> participators = participatorRepository.findByIds(ids);

        return CursorPage.of(participators, nextCursor, size);

    }

    @Override
    public void remove(BlockRecord blockRecord) {
        blockRecordJpaRepository.delete(blockRecord);
    }
}
