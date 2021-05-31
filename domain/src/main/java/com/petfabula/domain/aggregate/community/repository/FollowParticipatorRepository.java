package com.petfabula.domain.aggregate.community.repository;

import com.petfabula.domain.aggregate.community.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.entity.Participator;
import com.petfabula.domain.common.paging.CursorPage;

public interface FollowParticipatorRepository {

    CursorPage<Participator> findFollowed(Long authorId, Long cursor, int size);

    FollowParticipator find(Long followerId, Long followedId);

    FollowParticipator save(FollowParticipator followParticipator);

    void remove(FollowParticipator followParticipator);
}
