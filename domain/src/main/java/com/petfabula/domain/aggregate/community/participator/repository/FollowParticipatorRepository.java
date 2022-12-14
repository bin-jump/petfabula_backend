package com.petfabula.domain.aggregate.community.participator.repository;

import com.petfabula.domain.aggregate.community.participator.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.paging.CursorPage;

public interface FollowParticipatorRepository {

    CursorPage<Participator> findFollowed(Long participatorId, Long cursor, int size);

    CursorPage<Participator> findFollower(Long participatorId, Long cursor, int size);

    FollowParticipator find(Long followerId, Long followedId);

    FollowParticipator save(FollowParticipator followParticipator);

    void remove(FollowParticipator followParticipator);
}
