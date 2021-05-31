package com.petfabula.domain.aggregate.community.repository;

import com.petfabula.domain.aggregate.community.entity.Post;
import com.petfabula.domain.aggregate.community.entity.PostTopicRelation;
import com.petfabula.domain.common.paging.CursorPage;


public interface PostTopicRelationRepository {

    PostTopicRelation save(PostTopicRelation postTopicRelation);

    PostTopicRelation findByPostId(Long postId);

    CursorPage<Post> findPostsByTopic(Long topicId, Long cursor, int size);
}
