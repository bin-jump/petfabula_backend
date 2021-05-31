package com.petfabula.infrastructure.persistence.jpa.community.impl;

import com.petfabula.domain.aggregate.community.entity.LikePost;
import com.petfabula.domain.aggregate.community.repository.LikePostRepository;
import com.petfabula.infrastructure.persistence.jpa.community.repository.LikePostJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LikePostRepositoryImpl implements LikePostRepository {

    @Autowired
    private LikePostJpaRepository likePostJpaRepository;

    @Override
    public LikePost find(Long participatorId, Long postId) {
        return likePostJpaRepository.find(participatorId, postId);
    }

    @Override
    public LikePost save(LikePost likePost) {
        return null;
    }

    @Override
    public void remove(LikePost likePost) {

    }
}
