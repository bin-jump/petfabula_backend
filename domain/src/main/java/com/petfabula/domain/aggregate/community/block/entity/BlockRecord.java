package com.petfabula.domain.aggregate.community.block.entity;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "participator_block", uniqueConstraints={
        @UniqueConstraint(columnNames = {"participator_id", "target_id"})})
public class BlockRecord extends EntityBase {

    public BlockRecord(Long id, Long participatorId, Long targetId) {
        setId(id);
        this.targetId = targetId;
        this.participatorId = participatorId;
    }

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "participator_id", nullable = false)
    private Long participatorId;
}
