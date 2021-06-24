package com.petfabula.infrastructure.persistence.jpa.community.impl;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPost;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPostId;
import com.petfabula.domain.aggregate.community.post.repository.CollectPostRepository;
import com.petfabula.infrastructure.persistence.jpa.community.repository.CollectPostJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CollectPostRepositoryImpl implements CollectPostRepository {

    @Autowired
    private CollectPostJpaRepository collectPostJpaRepository;

    @Override
    public CollectPost save(CollectPost collectPost) {
        return collectPostJpaRepository.save(collectPost);
    }

    @Override
    public CollectPost find(Long participatorId, Long postId) {
        CollectPostId id = new CollectPostId(participatorId, postId);
        return collectPostJpaRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(CollectPost likePost) {
        collectPostJpaRepository.delete(likePost);
    }
}
