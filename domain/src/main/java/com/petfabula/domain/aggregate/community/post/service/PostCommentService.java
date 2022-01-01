package com.petfabula.domain.aggregate.community.post.service;

import com.petfabula.domain.aggregate.community.annotation.RestrictedAction;
import com.petfabula.domain.aggregate.community.guardian.service.RestrictionService;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostComment;
import com.petfabula.domain.aggregate.community.post.entity.PostCommentReply;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostCommentReplyRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostCommentRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostCommentService {

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostCommentReplyRepository postCommentReplyRepository;

    @Autowired
    private PostIdGenerator idGenerator;

    @RestrictedAction
    public PostComment createPostComment(Long participatorId, Long postId, String content) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_DEPEND_ENTITY);
        }

        post.setCommentCount(post.getCommentCount() + 1);
        PostComment postComment = new PostComment(idGenerator.nextId(), participator, postId, content);
        postRepository.save(post);

        return postCommentRepository.save(postComment);
    }

    public PostComment removePostComment(Long participatorId, Long postCommentId) {

        PostComment postComment = postCommentRepository.findById(postCommentId);
        if(postComment == null) {
            return null;
        }

        if (!postComment.getParticipator().getId().equals(participatorId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Post post = postRepository.findById(postComment.getPostId());
        if (post == null) {
            return null;
        }

        post.setCommentCount(post.getCommentCount() - 1 - postComment.getReplyCount());
        postCommentRepository.remove(postComment);
        postRepository.save(post);

        return postComment;
    }

    public PostCommentReply createCommentReply(Long participatorId, Long postCommentId, Long replyToId, String content) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        PostComment postComment = postCommentRepository.findById(postCommentId);
        if (postComment == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_DEPEND_ENTITY);
        }

        Post post = postRepository.findById(postComment.getPostId());
        if (post == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_DEPEND_ENTITY);
        }
        if (replyToId != null) {
            PostCommentReply replyTarget = postCommentReplyRepository.findById(replyToId);
            if (replyTarget == null) {
                throw new InvalidOperationException(CommonMessageKeys.NO_DEPEND_ENTITY);
            }
        }

        post.setCommentCount(post.getCommentCount() + 1);
        postComment.setReplyCount(postComment.getReplyCount() + 1);

        PostCommentReply reply = new PostCommentReply(idGenerator.nextId(), participator, postComment.getPostId(),
                postCommentId, replyToId, content);
        postCommentRepository.save(postComment);
        postRepository.save(post);

        return postCommentReplyRepository.save(reply);
    }

    public PostCommentReply removeCommentReply(Long participatorId, Long commentReplyId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        PostCommentReply commentReply = postCommentReplyRepository.findById(commentReplyId);
        if (commentReply == null) {
             return null;
        }

        if (!commentReply.getParticipator().getId().equals(participatorId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        PostComment postComment = postCommentRepository.findById(commentReply.getCommentId());
        if (postComment == null) {
            return null;
        }

        Post post = postRepository.findById(commentReply.getPostId());
        if (post == null) {
            return null;
        }

        post.setCommentCount(post.getCommentCount() - 1);
        postComment.setReplyCount(postComment.getReplyCount() - 1);

        postCommentRepository.save(postComment);
        postCommentReplyRepository.remove(commentReply);
        postRepository.save(post);

        return commentReply;
    }
}
