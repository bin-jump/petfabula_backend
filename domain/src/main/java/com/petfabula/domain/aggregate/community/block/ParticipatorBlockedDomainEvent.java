package com.petfabula.domain.aggregate.community.block;

import com.petfabula.domain.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class ParticipatorBlockedDomainEvent extends DomainEvent {

    public ParticipatorBlockedDomainEvent(Long participatorId, Long targetId) {
        this.participatorId = participatorId;
        this.targetId = targetId;
    }

    private Long participatorId;

    private Long targetId;
}
