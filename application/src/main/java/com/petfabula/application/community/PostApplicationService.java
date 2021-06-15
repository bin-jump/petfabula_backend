package com.petfabula.application.community;

import com.petfabula.domain.aggregate.community.participator.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.service.FollowService;
import com.petfabula.domain.aggregate.community.post.PostSearchService;
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
        return postCommentService.createPostComment(participatorId, postId, content);
    }

    @Transactional
    public PostComment removePostComment(Long participatorId, Long postCommentId) {
        return postCommentService.removePostComment(participatorId, postCommentId);
    }

    @Transactional
    public PostCommentReply createReplyComment(Long participatorId, Long postCommentId, String content) {
        return postCommentService.createCommentReply(participatorId, postCommentId, content);
    }

    @Transactional
    public PostCommentReply removeCommentReply(Long participatorId, Long commentReplyId) {
        return postCommentService.removeCommentReply(participatorId, commentReplyId);
    }

    @Transactional
    public FollowParticipator follow(Long followerId, Long followedId) {
        return followService.follow(followerId, followedId);
    }

    @Transactional
    public FollowParticipator unfollow(Long followerId, Long followedId){
        return followService.unfollow(followerId, followedId);
    }

    @Transactional
    public LikePost likePost(Long participatorId, Long postId) {
        return likeService.likePost(participatorId, postId);
    }

    @Transactional
    public LikePost removelikePost(Long participatorId, Long postId) {
        return likeService.removelikePost(participatorId, postId);
    }

}
