package com.petfabula.domain.aggregate.community.repository;

import com.petfabula.domain.aggregate.community.entity.Post;
import com.petfabula.domain.common.paging.CursorPage;

public interface RecommandPostRepository {

    CursorPage<Post> findRecentRecommand(Long cursor, int size);
}
