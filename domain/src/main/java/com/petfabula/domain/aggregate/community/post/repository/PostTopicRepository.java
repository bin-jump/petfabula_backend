package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;

import java.util.List;

public interface PostTopicRepository {

    PostTopicCategory save(PostTopicCategory postTopicCategory);

    PostTopic save(PostTopic postTopic);

    List<PostTopicCategory> findAll();

    PostTopic findById(Long id);

    PostTopicCategory findByTitle(String title);
}
