package com.petfabula.domain.aggregate.community.service;

import com.petfabula.domain.aggregate.community.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.entity.Participator;
import com.petfabula.domain.aggregate.community.error.PostMessageKeys;
import com.petfabula.domain.aggregate.community.repository.FollowParticipatorRepository;
import com.petfabula.domain.aggregate.community.repository.ParticipatorRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private FollowParticipatorRepository followParticipatorRepository;

    public FollowParticipator follow(Long followerId, Long followedId) {
        Participator follower = participatorRepository.findById(followerId);
        if (follower == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_FOLLOW_PARTICIPATOR);
        }

        Participator followed = participatorRepository.findById(followedId);
        if (followed == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_FOLLOW_PARTICIPATOR);
        }

        FollowParticipator followParticipator = followParticipatorRepository.find(followerId, followedId);
        if (followParticipator == null) {
            followParticipator = new FollowParticipator(follower, followed);
            follower.setFollowedCount(follower.getFollowedCount() + 1);
            followed.setFollowerCount(followed.getFollowerCount() + 1);
            participatorRepository.save(follower);
            participatorRepository.save(followed);
            followParticipatorRepository.save(followParticipator);
        }

        return followParticipator;
    }

    public FollowParticipator unfollow(Long followerId, Long followedId) {
        Participator follower = participatorRepository.findById(followerId);
        if (follower == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_UNFOLLOW_PARTICIPATOR);
        }

        Participator followed = participatorRepository.findById(followedId);
        if (followed == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_UNFOLLOW_PARTICIPATOR);
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
