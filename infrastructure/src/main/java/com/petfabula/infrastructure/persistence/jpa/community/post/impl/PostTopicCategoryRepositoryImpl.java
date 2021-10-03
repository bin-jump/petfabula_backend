package com.petfabula.infrastructure.persistence.jpa.community.post.impl;

import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;
import com.petfabula.domain.aggregate.community.post.repository.PostTopicCategoryRepository;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.PostTopicCategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostTopicCategoryRepositoryImpl implements PostTopicCategoryRepository {

    @Autowired
    private PostTopicCategoryJpaRepository postTopicCategoryJpaRepository;

    @Override
    public PostTopicCategory findById(Long id) {
        return postTopicCategoryJpaRepository.findById(id).orElse(null);
    }

    @Override
    public PostTopicCategory findByTitle(String title) {
        return postTopicCategoryJpaRepository.findByTitle(title);
    }

    @Override
    public List<PostTopicCategory> findAll() {
        return postTopicCategoryJpaRepository.findAll();
    }

    @Override
    public PostTopicCategory save(PostTopicCategory postTopicCategory) {
        return postTopicCategoryJpaRepository.save(postTopicCategory);
    }

    @Override
    public void remove(PostTopicCategory postTopicCategory) {
        postTopicCategoryJpaRepository.delete(postTopicCategory);
    }
}
