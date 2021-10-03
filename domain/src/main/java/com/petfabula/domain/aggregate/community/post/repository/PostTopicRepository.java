package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;

import java.util.List;

public interface PostTopicRepository {

    PostTopic findById(Long id);

    PostTopic save(PostTopic postTopic);

    List<PostTopic> findAll();

    PostTopic findByTitle(String title);

    void updateCategoryTitle(Long categoryId, String title);

    void removeByCategoryId(Long categoryId);

    void remove(PostTopic postTopic);
}
