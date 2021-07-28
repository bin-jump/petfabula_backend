package com.petfabula.application.community;

import com.petfabula.application.event.*;
import com.petfabula.domain.aggregate.community.participator.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.service.FollowService;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPost;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePost;
import com.petfabula.domain.aggregate.community.post.entity.*;
import com.petfabula.domain.aggregate.community.post.service.*;
import com.petfabula.domain.common.image.ImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PostApplicationService {

    @Autowired
    private PostService postService;

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    private FollowService followService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private IntegratedEventPublisher eventPublisher;

    @Transactional
    public Post createPost(Long participatorId, String content, Long relatePetId, Long topicId, List<ImageFile> images) {
        return postService.create(participatorId, content, relatePetId, topicId, images);
    }

    @Transactional
    public Post removePost(Long participatorId, Long postId) {
        return postService.remove(participatorId, postId);
    }

    @Transactional
    public PostComment createPostComment(Long participatorId, Long postId, String content) {
        PostComment postComment =
                postCommentService.createPostComment(participatorId, postId, content);
        eventPublisher.publish(new PostCommentCreateEvent(postComment));
        return postComment;
    }

    @Transactional
    public PostComment removePostComment(Long participatorId, Long postCommentId) {
        return postCommentService.removePostComment(participatorId, postCommentId);
    }

    @Transactional
    public PostCommentReply createReplyComment(Long participatorId, Long postCommentId, Long replyToId, String content) {
        PostCommentReply commentReply =
                postCommentService.createCommentReply(participatorId, postCommentId, replyToId, content);
        eventPublisher.publish(new PostCommentReplyCreateEvent(commentReply));
        return commentReply;
    }

    @Transactional
    public PostCommentReply removeCommentReply(Long participatorId, Long commentReplyId) {
        return postCommentService.removeCommentReply(participatorId, commentReplyId);
    }

    @Transactional
    public LikePost likePost(Long participatorId, Long postId) {
        LikePost likePost = likeService.likePost(participatorId, postId);
        eventPublisher.publish(new PostLikedEvent(likePost.getPostId(), participatorId));

        return likePost;
    }

    @Transactional
    public LikePost removelikePost(Long participatorId, Long postId) {
        return likeService.removelikePost(participatorId, postId);
    }

    @Transactional
    public CollectPost collectPost(Long participatorId, Long postId) {
        return collectService.collect(participatorId, postId);
    }

    @Transactional
    public CollectPost removeCollectPost(Long participatorId, Long postId) {
        return collectService.removeCollect(participatorId, postId);
    }

    @Transactional
    public FollowParticipator follow(Long followerId, Long followedId) {
        FollowParticipator followParticipator = followService.follow(followerId, followedId);
        eventPublisher.publish(new ParticipatorFollowEvent(followParticipator.getFollowerId(),
                followParticipator.getFollowedId()));

        return followParticipator;
    }

    @Transactional
    public FollowParticipator unfollow(Long followerId, Long followedId){
        return followService.unfollow(followerId, followedId);
    }

}
