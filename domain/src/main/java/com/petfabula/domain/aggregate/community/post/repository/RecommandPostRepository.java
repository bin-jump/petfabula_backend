package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.common.paging.CursorPage;

public interface RecommandPostRepository {

    CursorPage<Post> findRecentRecommand(Long cursor, int size);
}
