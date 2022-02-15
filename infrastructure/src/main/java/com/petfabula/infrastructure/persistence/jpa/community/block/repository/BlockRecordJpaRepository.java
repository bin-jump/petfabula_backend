package com.petfabula.infrastructure.persistence.jpa.community.block.repository;

import com.petfabula.domain.aggregate.community.block.entity.BlockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlockRecordJpaRepository extends JpaRepository<BlockRecord, Long>, JpaSpecificationExecutor {

    BlockRecord findByParticipatorIdAndTargetId(Long participatorId, Long targetId);
}
