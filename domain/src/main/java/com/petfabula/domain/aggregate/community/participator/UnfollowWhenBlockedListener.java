package com.petfabula.domain.aggregate.community.participator;

import com.petfabula.domain.aggregate.community.block.ParticipatorBlockedDomainEvent;
import com.petfabula.domain.aggregate.community.participator.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UnfollowWhenBlockedListener {

    @Autowired
    private FollowService followService;

    @EventListener
    public void handle(ParticipatorBlockedDomainEvent event) {
        followService.unfollow(event.getTargetId(), event.getParticipatorId());
    }
}
