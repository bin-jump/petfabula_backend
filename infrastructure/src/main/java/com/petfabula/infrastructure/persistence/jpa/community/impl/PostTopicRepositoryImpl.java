package com.petfabula.infrastructure.persistence.jpa.community.impl;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicCategory;
import com.petfabula.domain.aggregate.community.post.repository.PostTopicRepository;
import com.petfabula.infrastructure.persistence.jpa.community.repository.PostTopicCategoryJpaRepository;
import com.petfabula.infrastructure.persistence.jpa.community.repository.PostTopicJpaRepository;
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
    public List<PostTopicCategory> findAll() {
        return postTopicCategoryJpaRepository.findAll();
    }

    @Override
    public PostTopic findById(Long id) {
        return postTopicJpaRepository.findById(id).orElse(null);
    }

    @Override
    public PostTopicCategory findByTitle(String title) {
        return postTopicCategoryJpaRepository.findByTitle(title);
    }
}
