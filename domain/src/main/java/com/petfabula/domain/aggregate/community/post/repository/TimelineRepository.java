package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.common.paging.CursorPage;

public interface TimelineRepository {

    CursorPage<Post> findFollowedByParticipatorId(Long participatorId, Long cursor, int size);
}
