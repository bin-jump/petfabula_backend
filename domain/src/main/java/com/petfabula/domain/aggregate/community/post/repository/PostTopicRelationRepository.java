package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicRelation;
import com.petfabula.domain.common.paging.CursorPage;


public interface PostTopicRelationRepository {

    PostTopicRelation save(PostTopicRelation postTopicRelation);

    PostTopicRelation findByPostId(Long postId);

    CursorPage<Post> findPostsByTopic(Long topicId, Long cursor, int size);
}
