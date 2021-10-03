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

    @Override
    public void updateCategoryTitle(Long categoryId, String title) {
        postTopicJpaRepository.updateCategoryTitle(categoryId, title);
    }

    @Override
    public void removeByCategoryId(Long categoryId) {
        postTopicJpaRepository.removeByCategoryId(categoryId);
    }

    @Override
    public void remove(PostTopic postTopic) {
        postTopicJpaRepository.delete(postTopic);
    }
}
