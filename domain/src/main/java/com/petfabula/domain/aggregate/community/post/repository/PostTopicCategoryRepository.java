package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;

import java.util.List;

public interface PostTopicCategoryRepository {

    PostTopicCategory findById(Long id);

    PostTopicCategory findByTitle(String title);

    List<PostTopicCategory> findAll();

    PostTopicCategory save(PostTopicCategory postTopicCategory);

    void remove(PostTopicCategory postTopicCategory);
}
