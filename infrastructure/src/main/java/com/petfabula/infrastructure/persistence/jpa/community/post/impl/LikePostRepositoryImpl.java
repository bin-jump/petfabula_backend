package com.petfabula.infrastructure.persistence.jpa.community.post.impl;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePost;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePostId;
import com.petfabula.domain.aggregate.community.post.repository.LikePostRepository;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.LikePostJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LikePostRepositoryImpl implements LikePostRepository {

    @Autowired
    private LikePostJpaRepository likePostJpaRepository;

    @Override
    public LikePost find(Long participatorId, Long postId) {
        LikePostId likePostId = new LikePostId(participatorId, postId);
        return likePostJpaRepository.findById(likePostId).orElse(null);
    }

    @Override
    public LikePost save(LikePost likePost) {
        return likePostJpaRepository.save(likePost);
    }

    @Override
    public void remove(LikePost likePost) {
        likePostJpaRepository.delete(likePost);
    }
}
