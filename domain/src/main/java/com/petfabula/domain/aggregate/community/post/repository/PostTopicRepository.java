package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;

import java.util.List;

public interface PostTopicRepository {

    PostTopic save(PostTopic postTopic);

    List<PostTopic> findAll();

    PostTopic findById(Long id);

    PostTopic findByTitle(String title);
}
