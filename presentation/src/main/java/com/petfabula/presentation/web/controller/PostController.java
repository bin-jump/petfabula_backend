package com.petfabula.presentation.web.controller;

import com.petfabula.application.service.PostApplicationService;
import com.petfabula.domain.aggregate.community.entity.*;
import com.petfabula.domain.aggregate.community.error.PostMessageKeys;
import com.petfabula.domain.aggregate.community.repository.*;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.domain.exception.NotFoundException;
import com.petfabula.presentation.facade.assembler.community.*;
import com.petfabula.presentation.facade.dto.community.*;
import com.petfabula.presentation.web.api.CursorPageData;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@Validated
public class PostController {

    static final int DEAULT_PAGE_SIZE = 5;

    @Autowired
    private PostAssembler postAssembler;

    @Autowired
    private PostCommentAssembler postCommentAssembler;

    @Autowired
    private PostCommentReplyAssembler postCommentReplyAssembler;

    @Autowired
    private ParticiptorPetAssembler participtorPetAssembler;

    @Autowired
    private PostTopicAssembler postTopicAssembler;

    @Autowired
    private PostApplicationService postApplicationService;

    @Autowired
    private RecommandPostRepository recommandPostRepository;

    @Autowired
    private TimelineRepository timeLineRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostCommentReplyRepository postCommentReplyRepository;

    @Autowired
    private PostTopicRelationRepository postTopicRelationRepository;

    @Autowired
    private ParticipatorPetRepository participatorPetRepository;

    @Autowired
    private PostTopicRepository postTopicRepository;

    @GetMapping("recommends")
    public Response<CursorPageData<PostDto>> getRecommandPosts(@RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Post> posts = recommandPostRepository.findRecentRecommand(cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts.getResult()), posts.isHasMore(),
                        posts.getPageSize(), posts.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("followed")
    public Response<CursorPageData<PostDto>> geTimelinePosts(@RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        CursorPage<Post> posts = timeLineRepository.findFollowedByParticipatorId(userId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts.getResult()), posts.isHasMore(),
                        posts.getPageSize(), posts.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("user/{userId}/posts")
    public Response<CursorPageData<PostDto>> getUserPosts(@PathVariable("userId") Long userId,
                                                          @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<Post> posts = postRepository.findByParticipatorId(userId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts.getResult()), posts.isHasMore(),
                        posts.getPageSize(), posts.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("posts")
    public Response<CursorPageData<PostDto>> getMyPosts(@RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        CursorPage<Post> posts = postRepository.findByParticipatorId(userId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts.getResult()), posts.isHasMore(),
                        posts.getPageSize(), posts.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("posts/{postId}")
    public Response<PostDto> getPostDetail(@PathVariable("postId") Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new NotFoundException(PostMessageKeys.POST_NOT_FOUND);
        }
        PostDto res = postAssembler.convertToDto(post);
        Long userId = LoginUtils.currentUserId();
        if (userId != null) {
            LikePost likePost = likePostRepository.find(postId, userId);
            res.setLiked(likePost != null);
        }
        if (post.getRelatePetId() != null) {
            res.setParticipatorPet(participtorPetAssembler
                    .convertToDto(participatorPetRepository.findById(post.getRelatePetId())));
        }
        PostTopicRelation postTopicRelation =
                postTopicRelationRepository.findByPostId(post.getId());
        if (postTopicRelation != null) {
            res.setPostTopic(postTopicAssembler.convertToDto(postTopicRelation.getPostTopic()));
        }
        return Response.ok(res);
    }

    @PostMapping("/posts")
    public Response<PostDto> createPost(@RequestPart(name = "post") @Validated PostDto postDto,
                                        @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        Long userId = LoginUtils.currentUserId();
        List<ImageFile> imageFiles = new ArrayList<>();
        if (images != null) {
            for (MultipartFile file : images) {
                imageFiles.add(new ImageFile(file.getOriginalFilename(),
                        file.getInputStream(), file.getSize()));
            }
        }
        Post post = postApplicationService
                .createPost(userId, postDto.getContent(), postDto.getRelatePetId(), postDto.getTopicId(), imageFiles);
        postDto = postAssembler.convertToDto(post);
        return Response.ok(postDto);
    }

    @DeleteMapping("posts/{postId}")
    public Response<Long> removePost(@PathVariable Long postId) {
        Long userId = LoginUtils.currentUserId();
        postApplicationService.removePost(userId, postId);
        return Response.ok(postId);
    }

    @PostMapping("/comments")
    public Response<PostCommentDto> createPostComment(@RequestBody @Validated PostCommentDto postCommentDto) {
        Long userId = LoginUtils.currentUserId();
        PostComment postComment = postApplicationService
                .createPostComment(userId, postCommentDto.getPostId(), postCommentDto.getContent());
        postCommentDto = postCommentAssembler.convertToDto(postComment);
        return Response.ok(postCommentDto);
    }

    @GetMapping("posts/{postId}/comments")
    public Response<CursorPageData<PostCommentDto>> getPostComments(@PathVariable("postId") Long postId,
                                                                    @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<PostComment> comments = postCommentRepository.findByPostId(postId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostCommentDto> res = CursorPageData
                .of(postCommentAssembler.convertToDtos(comments.getResult()), comments.isHasMore(),
                        comments.getPageSize(), comments.getNextCursor());

        return Response.ok(res);
    }

    @DeleteMapping("comments/{commentId}")
    public Response<Long> removePostComment(@PathVariable Long commentId) {
        Long userId = LoginUtils.currentUserId();
        postApplicationService.removePostComment(userId, commentId);
        return Response.ok(commentId);
    }

    @PostMapping("/replies")
    public Response<PostCommentReplyDto> createCommentReply(@RequestBody @Validated PostCommentReplyDto postCommentReplyDto) {
        Long userId = LoginUtils.currentUserId();
        PostCommentReply commentReply = postApplicationService
                .createReplyComment(userId, postCommentReplyDto.getPostCommentId(), postCommentReplyDto.getContent());
        postCommentReplyDto = postCommentReplyAssembler.convertToDto(commentReply);
        return Response.ok(postCommentReplyDto);
    }

    @DeleteMapping("replies/{replyId}")
    public Response<Long> removePostCommentReply(@PathVariable Long replyId) {
        Long userId = LoginUtils.currentUserId();
        postApplicationService.removeCommentReply(userId, replyId);
        return Response.ok(replyId);
    }

    @GetMapping("comments/{commentId}/replies")
    public Response<CursorPageData<PostCommentReplyDto>> getCommentReply(@PathVariable("commentId") Long commentId,
                                                                         @RequestParam(value = "cursor", required = false) Long cursor) {
        CursorPage<PostCommentReply> replies = postCommentReplyRepository.findByPostComment(commentId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostCommentReplyDto> res = CursorPageData
                .of(postCommentReplyAssembler.convertToDtos(replies.getResult()), replies.isHasMore(),
                        replies.getPageSize(), replies.getNextCursor());
        return Response.ok(res);
    }

    @PostMapping("authors/{userId}/follow")
    public Response<FollowResult> followAuthor(@PathVariable("userId") Long participatorId) {
        Long userId = LoginUtils.currentUserId();
        FollowParticipator followParticipator = postApplicationService.follow(userId, participatorId);
        FollowResult res = FollowResult.builder()
                .authorId(participatorId)
                .followed(true)
                .build();
        return Response.ok(res);
    }

    @DeleteMapping("authors/{userId}/follow")
    public Response<FollowResult> unfollowAuthor(@PathVariable("userId") Long participatorId) {
        Long userId = LoginUtils.currentUserId();
        FollowParticipator follow = postApplicationService.unfollow(userId, participatorId);
        FollowResult res = FollowResult.builder()
                .authorId(participatorId)
                .followed(false)
                .build();
        return Response.ok(res);
    }

    @PostMapping("posts/{postId}/like")
    public Response<LikeResult> likePost(@PathVariable("postId") Long postId) {
        Long userId = LoginUtils.currentUserId();
        LikePost likePost = postApplicationService.likePost(userId, postId);
        LikeResult res = LikeResult.builder()
                .postId(postId)
                .liked(true)
                .build();
        return Response.ok(res);
    }

    @DeleteMapping("posts/{postId}/like")
    public Response<LikeResult> removelikePost(@PathVariable("postId") Long postId) {
        Long userId = LoginUtils.currentUserId();
        LikePost likePost = postApplicationService.removelikePost(userId, postId);
        LikeResult res = LikeResult.builder()
                .postId(postId)
                .liked(false)
                .build();
        return Response.ok(res);
    }

    @GetMapping("topics")
    public Response<List<PostTopicDto>> getAllTopics() {
        List<PostTopic> postTopics = postTopicRepository.findAll();
        List<PostTopicDto> res = postTopicAssembler.convertToDtos(postTopics);
        return Response.ok(res);
    }
}
