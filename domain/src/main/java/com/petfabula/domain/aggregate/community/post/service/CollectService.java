package com.petfabula.domain.aggregate.community.post.service;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.PostMessageKeys;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPost;
import com.petfabula.domain.aggregate.community.post.repository.CollectPostRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectService {

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CollectPostRepository collectPostRepository;

    public CollectPost collect(Long participatorId, Long postId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new InvalidOperationException(PostMessageKeys.POST_NOT_FOUND);
        }

        CollectPost collectPost = collectPostRepository.find(participatorId, postId);
        if (collectPost == null) {
            collectPost = new CollectPost(participator, post);
            collectPost = collectPostRepository.save(collectPost);
            participator.setCollectCount(participator.getCollectCount() + 1);
            post.setCollectCount(post.getCollectCount() + 1);
            postRepository.save(post);
            participatorRepository.save(participator);
        }

        return collectPost;
    }

    public void removeCollect(Long participatorId, Long postId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new InvalidOperationException(PostMessageKeys.POST_NOT_FOUND);
        }

        CollectPost collectPost = collectPostRepository.find(participatorId, postId);
        if (collectPost != null) {
            collectPostRepository.remove(collectPost);
            participator.setCollectCount(participator.getCollectCount() - 1);
            post.setCollectCount(post.getCollectCount() - 1);
            postRepository.save(post);
            participatorRepository.save(participator);
        }
    }
}