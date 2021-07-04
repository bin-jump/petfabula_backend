package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPost;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPostId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectPostJpaRepository extends JpaRepository<CollectPost, CollectPostId> {

}
