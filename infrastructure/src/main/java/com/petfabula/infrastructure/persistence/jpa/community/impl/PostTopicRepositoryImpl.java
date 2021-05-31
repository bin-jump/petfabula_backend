package com.petfabula.infrastructure.persistence.jpa.community.impl;

import com.petfabula.domain.aggregate.community.entity.PostTopic;
import com.petfabula.domain.aggregate.community.repository.PostTopicRepository;
import com.petfabula.infrastructure.persistence.jpa.community.repository.PostTopicJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostTopicRepositoryImpl implements PostTopicRepository {

    @Autowired
    private PostTopicJpaRepository postTopicJpaRepository;

    @Override
    public PostTopic save(PostTopic postTopic) {
        return postTopicJpaRepository.save(postTopic);
    }

    @Override
    public List<PostTopic> findAll() {
        return postTopicJpaRepository.findAll();
    }

    @Override
    public PostTopic findById(Long id) {
        return postTopicJpaRepository.findById(id).orElse(null);
    }

    @Override
    public PostTopic findByTitle(String title) {
        return postTopicJpaRepository.findByTitle(title);
    }
}
