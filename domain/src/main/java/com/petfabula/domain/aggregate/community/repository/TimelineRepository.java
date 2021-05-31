package com.petfabula.domain.aggregate.community.repository;

import com.petfabula.domain.aggregate.community.entity.Post;
import com.petfabula.domain.common.paging.CursorPage;

public interface TimelineRepository {

    CursorPage<Post> findFollowedByParticipatorId(Long participatorId, Long cursor, int size);
}
