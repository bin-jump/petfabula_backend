package com.petfabula.domain.aggregate.community.service;

import com.petfabula.domain.aggregate.community.entity.Participator;
import com.petfabula.domain.aggregate.community.entity.Post;
import com.petfabula.domain.aggregate.community.entity.PostComment;
import com.petfabula.domain.aggregate.community.entity.PostCommentReply;
import com.petfabula.domain.aggregate.community.error.PostMessageKeys;
import com.petfabula.domain.aggregate.community.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.repository.PostCommentReplyRepository;
import com.petfabula.domain.aggregate.community.repository.PostCommentRepository;
import com.petfabula.domain.aggregate.community.repository.PostRepository;
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

    public PostComment createPostComment(Long participatorId, Long postId, String content) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_COMMENT);
        }

        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_COMMENT);
        }

        post.setCommentCount(post.getCommentCount() + 1);
        PostComment postComment = new PostComment(idGenerator.nextId(), participator, postId, content);
        postRepository.save(post);

        return postCommentRepository.save(postComment);
    }

    public PostComment removePostComment(Long participatorId, Long postCommentId) {

        PostComment postComment = postCommentRepository.findById(postCommentId);
        if (postComment == null || !postComment.getParticipator().getId().equals(participatorId)) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_REMOVE_POST_COMMENT);
        }

        Post post = postRepository.findById(postComment.getPostId());
        if (post == null) {
            //throw new InvalidOperationException(PostMessageKeys.POST_COMMENT_NOT_FOUND);
            return null;
        }

        post.setCommentCount(post.getCommentCount() - 1);
        postCommentRepository.remove(postComment);
        postRepository.save(post);

        return postComment;
    }

    public PostCommentReply createCommentReply(Long participatorId, Long postCommentId, String content) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_POST_COMMENT_REPLY);
        }

        PostComment postComment = postCommentRepository.findById(postCommentId);
        if (postComment == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_POST_COMMENT_REPLY);
        }

        Post post = postRepository.findById(postComment.getPostId());
        if (post == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_POST_COMMENT_REPLY);
        }

        post.setCommentCount(post.getCommentCount() + 1);
        postComment.setReplyCount(postComment.getReplyCount() + 1);

        PostCommentReply reply = new PostCommentReply(idGenerator.nextId(), participator, postCommentId,
                postComment.getPostId(), content);
        postCommentRepository.save(postComment);
        postRepository.save(post);

        return postCommentReplyRepository.save(reply);
    }

    public PostCommentReply removeCommentReply(Long participatorId, Long commentReplyId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_REMOVE_COMMENT_REPLY);
        }

        PostCommentReply commentReply = postCommentReplyRepository.findById(commentReplyId);
        if (commentReply == null) {
             return null;
        }

        if (!commentReply.getParticipator().getId().equals(participatorId)) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_REMOVE_COMMENT_REPLY);
        }

        PostComment postComment = postCommentRepository
                .findById(commentReply.getPostCommentId());
        if (postComment == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_REMOVE_COMMENT_REPLY);
        }

        Post post = postRepository.findById(commentReply.getPostId());
        if (post == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_CREATE_REMOVE_COMMENT_REPLY);
        }

        post.setCommentCount(post.getCommentCount() - 1);
        postComment.setReplyCount(postComment.getReplyCount() - 1);

        postCommentRepository.save(postComment);
        postCommentReplyRepository.remove(commentReply);
        postRepository.save(post);

        return commentReply;
    }
}
