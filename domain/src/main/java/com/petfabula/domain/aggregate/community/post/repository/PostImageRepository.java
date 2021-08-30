package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostImage;
import com.petfabula.domain.common.paging.CursorPage;

public interface PostImageRepository {

    CursorPage<PostImage> findByPetId(Long petId, Long cursor, int size);

    void remove(PostImage image);
}
