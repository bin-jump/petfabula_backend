package com.petfabula.domain.aggregate.community.participator.service;

import com.petfabula.domain.aggregate.community.block.service.BlockRecordService;
import com.petfabula.domain.aggregate.community.participator.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.ParticipatorMessageKeys;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.FollowParticipatorRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.service.PostIdGenerator;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private BlockRecordService blockRecordService;

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private FollowParticipatorRepository followParticipatorRepository;

    @Autowired
    private PostIdGenerator idGenerator;

    public FollowParticipator follow(Long followerId, Long followedId) {
        if (followedId.equals(followerId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        Participator follower = participatorRepository.findById(followerId);
        if (follower == null) {
            throw new InvalidOperationException(ParticipatorMessageKeys.CANNOT_FOLLOW_PARTICIPATOR);
        }

        Participator followed = participatorRepository.findById(followedId);
        if (followed == null) {
            throw new InvalidOperationException(ParticipatorMessageKeys.CANNOT_FOLLOW_PARTICIPATOR);
        }
        blockRecordService.guardBlock(followedId, followerId);

        FollowParticipator followParticipator = followParticipatorRepository.find(followerId, followedId);
        if (followParticipator == null) {
            followParticipator = new FollowParticipator(idGenerator.nextId(), follower, followed);
            follower.setFollowedCount(follower.getFollowedCount() + 1);
            followed.setFollowerCount(followed.getFollowerCount() + 1);
            participatorRepository.save(follower);
            participatorRepository.save(followed);
            followParticipatorRepository.save(followParticipator);
        }

        return followParticipator;
    }

    public FollowParticipator unfollow(Long followerId, Long followedId) {
        if (followedId.equals(followerId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        Participator follower = participatorRepository.findById(followerId);
        if (follower == null) {
            throw new InvalidOperationException(ParticipatorMessageKeys.CANNOT_UNFOLLOW_PARTICIPATOR);
        }

        Participator followed = participatorRepository.findById(followedId);
        if (followed == null) {
            throw new InvalidOperationException(ParticipatorMessageKeys.CANNOT_UNFOLLOW_PARTICIPATOR);
        }

        FollowParticipator followParticipator = followParticipatorRepository.find(followerId, followedId);
        if (followParticipator != null) {
            follower.setFollowedCount(follower.getFollowedCount() - 1);
            followed.setFollowerCount(followed.getFollowerCount() - 1);
            participatorRepository.save(follower);
            participatorRepository.save(followed);
            followParticipatorRepository.remove(followParticipator);
        }

        return followParticipator;
    }
}
