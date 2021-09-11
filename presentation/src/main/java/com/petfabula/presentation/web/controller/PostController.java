package com.petfabula.presentation.web.controller;

import com.petfabula.application.community.PostApplicationService;
import com.petfabula.domain.aggregate.community.participator.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.participator.repository.FollowParticipatorRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPost;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePost;
import com.petfabula.domain.aggregate.community.post.entity.*;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicRelation;
import com.petfabula.domain.aggregate.community.post.repository.*;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.NotFoundException;
import com.petfabula.presentation.facade.assembler.community.*;
import com.petfabula.presentation.facade.dto.AlreadyDeletedResponse;
import com.petfabula.presentation.facade.dto.ImageDto;
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
import java.util.stream.Collectors;

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
    private PostRepository postRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private CollectPostRepository collectPostRepository;

    @Autowired
    private FollowParticipatorRepository followParticipatorRepository;

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

    @GetMapping("posts/{postId}")
    public Response<PostDto> getPostDetail(@PathVariable("postId") Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new NotFoundException(postId, CommonMessageKeys.POST_NOT_FOUND);
        }
        PostDto res = postAssembler.convertToDto(post);
        Long userId = LoginUtils.currentUserId();
        if (userId != null) {
            LikePost likePost = likePostRepository.find(userId, postId);
            res.setLiked(likePost != null);
            if (!userId.equals(post.getParticipator().getId())) {
                CollectPost collectPost = collectPostRepository.find(userId, postId);
                res.setCollected(collectPost != null);
                FollowParticipator followParticipator = followParticipatorRepository
                        .find(userId, post.getParticipator().getId());
                res.getParticipator().setFollowed(followParticipator != null);
            }
        }

        if (post.getRelatePetId() != null) {
            ParticipatorPet pet = participatorPetRepository.findById(post.getRelatePetId());
            if (pet != null) {
                res.setRelatePet(participtorPetAssembler.convertToDto(pet));
            }
        }

        PostTopicRelation postTopicRelation = postTopicRelationRepository.findByPostId(post.getId());
        if (postTopicRelation != null) {
            Long topicId =postTopicRelation.getTopicId();
            PostTopic topic = postTopicRepository.findTopicById(topicId);
            if (topic != null) {
                res.setPostTopic(postTopicAssembler.convertToDto(topic));
            }
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
                if (file.isEmpty()) {
                    throw new InvalidOperationException("Empty image file");
                }
                imageFiles.add(new ImageFile(file.getOriginalFilename(),
                        file.getInputStream(), file.getSize()));
            }
        }
        Post post = postApplicationService
                .createPost(userId, postDto.getContent(),
                        postDto.getRelatePetId(), postDto.getTopicId(), imageFiles);
        postDto = postAssembler.convertToDto(post);
        return Response.ok(postDto);
    }


    @PutMapping("/posts")
    public Response<PostDto> updatePost(@RequestPart(name = "post") @Validated PostDto postDto,
                                        @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        Long userId = LoginUtils.currentUserId();
        List<ImageFile> imageFiles = new ArrayList<>();
        if (images != null) {
            for (MultipartFile file : images) {
                if (file.isEmpty()) {
                    throw new InvalidOperationException("Empty image file");
                }
                imageFiles.add(new ImageFile(file.getOriginalFilename(),
                        file.getInputStream(), file.getSize()));
            }
        }
        List<Long> imageIds = postDto.getImages().stream().map(ImageDto::getId).collect(Collectors.toList());
        Post post = postApplicationService
                .updatePost(userId, postDto.getId(), postDto.getContent(),
                        postDto.getRelatePetId(), postDto.getTopicId(), imageFiles, imageIds);
        postDto = postAssembler.convertToDto(post);

        LikePost likePost = likePostRepository.find(userId, post.getId());
        postDto.setLiked(likePost != null);
        if (!userId.equals(post.getParticipator().getId())) {
            CollectPost collectPost = collectPostRepository.find(userId, post.getId());
            postDto.setCollected(collectPost != null);
            FollowParticipator followParticipator = followParticipatorRepository
                    .find(userId, post.getParticipator().getId());
            postDto.getParticipator().setFollowed(followParticipator != null);
        }

        if (post.getRelatePetId() != null) {
            ParticipatorPet pet = participatorPetRepository.findById(post.getRelatePetId());
            if (pet != null) {
                postDto.setRelatePet(participtorPetAssembler.convertToDto(pet));
            }
        }

        PostTopicRelation postTopicRelation = postTopicRelationRepository.findByPostId(post.getId());
        if (postTopicRelation != null) {
            Long topicId =postTopicRelation.getTopicId();
            PostTopic topic = postTopicRepository.findTopicById(topicId);
            if (topic != null) {
                postDto.setPostTopic(postTopicAssembler.convertToDto(topic));
            }
        }

        return Response.ok(postDto);
    }

    @DeleteMapping("posts/{postId}")
    public Response<Object> removePost(@PathVariable Long postId) {
        Long userId = LoginUtils.currentUserId();
        Post post = postApplicationService.removePost(userId, postId);
        if (post == null) {
            return Response.ok(AlreadyDeletedResponse.of(postId));
        }
        PostDto res = postAssembler.convertToDto(post);
        return Response.ok(res);
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
    public Response<Object> removePostComment(@PathVariable Long commentId) {
        Long userId = LoginUtils.currentUserId();
        PostComment postComment = postApplicationService.removePostComment(userId, commentId);
        if (postComment == null) {
            return Response.ok(AlreadyDeletedResponse.of(commentId));
        }
        return Response.ok(postCommentAssembler.convertToDto(postComment));
    }

    @PostMapping("/replies")
    public Response<PostCommentReplyDto> createCommentReply(@RequestBody @Validated PostCommentReplyDto postCommentReplyDto) {
        Long userId = LoginUtils.currentUserId();
        PostCommentReply commentReply = postApplicationService
                .createReplyComment(userId, postCommentReplyDto.getCommentId(),
                        postCommentReplyDto.getReplyToId(), postCommentReplyDto.getContent());
        postCommentReplyDto = postCommentReplyAssembler.convertToDto(commentReply);
        return Response.ok(postCommentReplyDto);
    }

    @DeleteMapping("replies/{replyId}")
    public Response<Object> removePostCommentReply(@PathVariable Long replyId) {
        Long userId = LoginUtils.currentUserId();
        PostCommentReply commentReply = postApplicationService.removeCommentReply(userId, replyId);
        if (commentReply == null) {
            return Response.ok(AlreadyDeletedResponse.of(replyId));
        }
        return Response.ok(postCommentReplyAssembler.convertToDto(commentReply));
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

    @PostMapping("posts/{postId}/favorites")
    public Response<CollectPostResult> favoritePost(@PathVariable("postId") Long postId) {
        Long userId = LoginUtils.currentUserId();
        postApplicationService.collectPost(userId, postId);
        CollectPostResult res = CollectPostResult.builder()
                .postId(postId)
                .collected(true)
                .build();
        return Response.ok(res);
    }

    @DeleteMapping("posts/{postId}/favorites")
    public Response<CollectPostResult> removeFavoritePost(@PathVariable("postId") Long postId) {
        Long userId = LoginUtils.currentUserId();
        postApplicationService.removeCollectPost(userId, postId);
        CollectPostResult res = CollectPostResult.builder()
                .postId(postId)
                .collected(false)
                .build();
        return Response.ok(res);
    }

    @PostMapping("participator/{userId}/follow")
    public Response<FollowResult> followAuthor(@PathVariable("userId") Long participatorId) {
        Long userId = LoginUtils.currentUserId();
        FollowParticipator followParticipator = postApplicationService.follow(userId, participatorId);
        FollowResult res = FollowResult.builder()
                .participatorId(participatorId)
                .followed(true)
                .build();
        return Response.ok(res);
    }

    @DeleteMapping("participator/{userId}/follow")
    public Response<FollowResult> unfollowAuthor(@PathVariable("userId") Long participatorId) {
        Long userId = LoginUtils.currentUserId();
        FollowParticipator follow = postApplicationService.unfollow(userId, participatorId);
        FollowResult res = FollowResult.builder()
                .participatorId(participatorId)
                .followed(false)
                .build();
        return Response.ok(res);
    }

    @GetMapping("topics")
    public Response<List<PostTopicDto>> getAllTopics() {
        List<PostTopic> topics = postTopicRepository.findAllTopics();
        List<PostTopicDto> res = postTopicAssembler
                .convertToTopicDtos(topics);
        return Response.ok(res);
    }

    @GetMapping("topic/{topicId}/posts")
    public Response<CursorPageData<PostDto>> getTopicPosts(@PathVariable("topicId") Long topicId,
                                                        @RequestParam(value = "cursor", required = false) Long cursor) {

        CursorPage<Post> posts = postTopicRelationRepository.findPostsByTopic(topicId, cursor, DEAULT_PAGE_SIZE);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts.getResult()), posts.isHasMore(),
                        posts.getPageSize(), posts.getNextCursor());
        return Response.ok(res);
    }
}
