package com.petfabula.infrastructure.persistence.jpa.community.participator.repository;

import com.petfabula.domain.aggregate.community.participator.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.entity.FollowParticipatorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowParticipatorJpaRepository extends JpaRepository<FollowParticipator, FollowParticipatorId>, JpaSpecificationExecutor {

//    @Query("select fp from FollowParticipator fp where fp.followParticipatorId.followerId = :followerId " +
//        " and fp.followParticipatorId.followedId = :followedId")
//    FollowParticipator find(@Param("followerId")Long followerId, @Param("followedId")Long followedId);

    FollowParticipator findByFollowerIdAndFollowedId(Long followerId, Long followedId);
}
