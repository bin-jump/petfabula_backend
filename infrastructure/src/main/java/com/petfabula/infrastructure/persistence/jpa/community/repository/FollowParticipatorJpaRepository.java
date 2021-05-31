package com.petfabula.infrastructure.persistence.jpa.community.repository;

import com.petfabula.domain.aggregate.community.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.entity.valueobject.FollowParticipatorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowParticipatorJpaRepository extends JpaRepository<FollowParticipator, FollowParticipatorId>, JpaSpecificationExecutor {

    @Query("select fp from FollowParticipator fp where fp.followParticipatorId.followerId = :followerId " +
        " and fp.followParticipatorId.followedId = :followedId")
    FollowParticipator find(@Param("followerId")Long followerId, @Param("followedId")Long followedId);
}
