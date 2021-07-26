package com.petfabula.domain.aggregate.community.post.service;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePost;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.PostMessageKeys;
import com.petfabula.domain.aggregate.community.post.repository.LikePostRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private PostIdGenerator idGenerator;

    public LikePost likePost(Long participatorId, Long postId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_LIKE_POST);
        }

        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_LIKE_POST);
        }

        LikePost likePost = likePostRepository.find(participatorId, postId);
        if (likePost == null) {
            post.setLikeCount(post.getLikeCount() + 1);
            likePost = likePostRepository.save(new LikePost(idGenerator.nextId(), participator, post));
            postRepository.save(post);
        }
        return likePost;
    }

    public LikePost removelikePost(Long participatorId, Long postId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_REMOVE_LIKE_POST);
        }

        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_REMOVE_LIKE_POST);
        }

        LikePost likePost = likePostRepository.find(participatorId, postId);
        if (likePost != null) {
            post.setLikeCount(post.getLikeCount() - 1);
            likePostRepository.remove(likePost);
            postRepository.save(post);
        }

        return likePost;
    }
}
