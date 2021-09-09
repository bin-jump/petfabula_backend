package com.petfabula.infrastructure.persistence.jpa.community.post.impl;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;
import com.petfabula.domain.aggregate.community.post.repository.PostTopicRepository;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.PostTopicCategoryJpaRepository;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.PostTopicJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostTopicRepositoryImpl implements PostTopicRepository {

    @Autowired
    private PostTopicJpaRepository postTopicJpaRepository;

    @Autowired
    private PostTopicCategoryJpaRepository postTopicCategoryJpaRepository;

    @Override
    public PostTopicCategory save(PostTopicCategory postTopicCategory) {
        return postTopicCategoryJpaRepository.save(postTopicCategory);
    }

    @Override
    public PostTopic save(PostTopic postTopic) {
        return postTopicJpaRepository.save(postTopic);
    }

    @Override
    public List<PostTopic> findAllTopics() {
        return postTopicJpaRepository.findAll();
    }

    @Override
    public PostTopic findTopicById(Long id) {
        return postTopicJpaRepository.findById(id).orElse(null);
    }

    @Override
    public PostTopicCategory findCategoryByTitle(String title) {
        return postTopicCategoryJpaRepository.findByTitle(title);
    }

    @Override
    public PostTopic findTopicByTitle(String title) {
        return postTopicJpaRepository.findByTitle(title);
    }
}
