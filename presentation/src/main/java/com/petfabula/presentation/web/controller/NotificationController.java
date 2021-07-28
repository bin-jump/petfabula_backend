package com.petfabula.presentation.web.controller;

import com.petfabula.application.notification.NotificationApplicationService;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostComment;
import com.petfabula.domain.aggregate.community.post.entity.PostCommentReply;
import com.petfabula.domain.aggregate.community.post.repository.PostCommentReplyRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostCommentRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.AnswerComment;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.AnswerCommentRepository;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.aggregate.notification.entity.AnswerCommentNotification;
import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;
import com.petfabula.domain.aggregate.notification.entity.ParticipatorFollowNotification;
import com.petfabula.domain.aggregate.notification.entity.UpvoteNotification;
import com.petfabula.domain.aggregate.notification.respository.AnswerCommentNotificationRepository;
import com.petfabula.domain.aggregate.notification.respository.NotificationReceiverRepository;
import com.petfabula.domain.aggregate.notification.respository.ParticipatorFollowNotificationRepository;
import com.petfabula.domain.aggregate.notification.respository.UpvoteNotificationRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.presentation.facade.assembler.community.*;
import com.petfabula.presentation.facade.dto.community.ParticipatorDto;
import com.petfabula.presentation.facade.dto.notification.*;
import com.petfabula.presentation.web.api.CursorPageData;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
@Validated
public class NotificationController {

    static final int DEAULT_PAGE_SIZE = 5;

    @Autowired
    private PostCommentAssembler postCommentAssembler;

    @Autowired
    private PostAssembler postAssembler;

    @Autowired
    private QuestionAssembler questionAssembler;

    @Autowired
    private PostCommentReplyAssembler postCommentReplyAssembler;

    @Autowired
    private AnswerCommentAssembler answerCommentAssembler;

    @Autowired
    private AnswerAssembler answerAssembler;

    @Autowired
    private ParticiptorAssembler participtorAssembler;

    @Autowired
    private NotificationApplicationService notificationApplicationService;

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    @Autowired
    private AnswerCommentNotificationRepository answerCommentNotificationRepository;

    @Autowired
    private ParticipatorFollowNotificationRepository participatorFollowNotificationRepository;

    @Autowired
    private UpvoteNotificationRepository upvoteNotificationRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostCommentReplyRepository postCommentReplyRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AnswerCommentRepository answerCommentRepository;

    @Autowired
    private ParticipatorRepository participatorRepository;


    @GetMapping("notifications")
    public Response<NotificationCheckResult> checkNotifications() {
        Long userId = LoginUtils.currentUserId();
        NotificationReceiver receiver =
                notificationReceiverRepository.findById(userId);

        NotificationCheckResult res =
                NotificationCheckResult.builder()
                        .receiverId(userId)
                        .answerCommentCount(receiver.getAnswerCommentUnreadCount())
                        .followCount(receiver.getParticipatorFollowUnreadCount())
                        .voteCount(receiver.getUpvoteUnreadCount())
                        .build();

        return Response.ok(res);
    }

    // TODO: make this parallel
    @GetMapping("answer-comment-notifications")
    public Response<CursorPageData<AnswerCommentNotificationDto>> getAnswerCommentNotifications(@RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        CursorPage<AnswerCommentNotification> answerCommentNotifications =
                answerCommentNotificationRepository
                        .findByOwnerId(userId, cursor, DEAULT_PAGE_SIZE);


        List<Long> questionIds = answerCommentNotifications.getResult()
                .stream()
                .filter(item -> item.getTargetEntityType() == AnswerCommentNotification.EntityType.QUESTION)
                .map(AnswerCommentNotification::getTargetEntityId)
                .collect(Collectors.toList());

        List<Long> answerIds = answerCommentNotifications.getResult()
                .stream()
                .filter(item -> item.getTargetEntityType() == AnswerCommentNotification.EntityType.ANSWER)
                .map(AnswerCommentNotification::getTargetEntityId)
                .collect(Collectors.toList());

        List<Long> postIds = answerCommentNotifications.getResult()
                .stream()
                .filter(item -> item.getTargetEntityType() == AnswerCommentNotification.EntityType.POST)
                .map(AnswerCommentNotification::getTargetEntityId)
                .collect(Collectors.toList());


        List<Long> postCommentIds = answerCommentNotifications.getResult()
                .stream()
                .filter(item -> item.getTargetEntityType() == AnswerCommentNotification.EntityType.POST_COMMENT)
                .map(AnswerCommentNotification::getTargetEntityId)
                .collect(Collectors.toList());

        List<Long> postCommentReplyIds = answerCommentNotifications.getResult()
                .stream()
                .filter(item -> item.getTargetEntityType() == AnswerCommentNotification.EntityType.POST_COMMENT_REPLY)
                .map(AnswerCommentNotification::getTargetEntityId)
                .collect(Collectors.toList());

        List<Long> answerCommnetIds = answerCommentNotifications.getResult()
                .stream()
                .filter(item -> item.getTargetEntityType() == AnswerCommentNotification.EntityType.ANSWER_COMMENT)
                .map(AnswerCommentNotification::getTargetEntityId)
                .collect(Collectors.toList());

        Map<Long, Question> questionMap = questionRepository.findByIds(questionIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
        Map<Long, Post> postMap = postRepository.findByIds(postIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
        Map<Long, Answer> answerMap = answerRepository.findByIds(answerIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
        Map<Long, PostComment> postCommentMap = postCommentRepository.findByIds(postCommentIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
        Map<Long, PostCommentReply> postCommentReplyMap = postCommentReplyRepository.findByIds(postCommentReplyIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
        Map<Long, AnswerComment> answerCommentMap = answerCommentRepository.findByIds(answerCommnetIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));

        List<Long> actorIds = answerCommentNotifications.getResult()
                .stream().map(AnswerCommentNotification::getActorId).collect(Collectors.toList());
        Map<Long, ParticipatorDto> actorMap = participatorRepository.findByIds(actorIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> participtorAssembler.convertToDto(item)));

        List<AnswerCommentNotificationDto> notificationDtos = new ArrayList<>();
        answerCommentNotifications.getResult()
                .stream().forEach(item -> {
            ParticipatorDto actor = actorMap.get(item.getActorId());
            if (item.getTargetEntityType() == AnswerCommentNotification.EntityType.QUESTION) {
                Question question = questionMap.get(item.getTargetEntityId());
                notificationDtos.add(AnswerCommentNotificationDto.of(item, questionAssembler.convertToDto(question), actor));

            } else if (item.getTargetEntityType() == AnswerCommentNotification.EntityType.POST) {
                Post post = postMap.get(item.getTargetEntityId());
                notificationDtos.add(AnswerCommentNotificationDto.of(item, postAssembler.convertToDto(post), actor));

            } else if (item.getTargetEntityType() == AnswerCommentNotification.EntityType.ANSWER) {
                Answer answer = answerMap.get(item.getTargetEntityId());
                notificationDtos.add(AnswerCommentNotificationDto.of(item, answerAssembler.convertToDto(answer), actor));

            } else if (item.getTargetEntityType() == AnswerCommentNotification.EntityType.POST_COMMENT) {
                PostComment postComment = postCommentMap.get(item.getTargetEntityId());
                notificationDtos.add(AnswerCommentNotificationDto.of(item, postCommentAssembler.convertToDto(postComment), actor));

            } else if (item.getTargetEntityType() == AnswerCommentNotification.EntityType.POST_COMMENT_REPLY) {
                PostCommentReply postCommentReply = postCommentReplyMap.get(item.getTargetEntityId());
                notificationDtos.add(AnswerCommentNotificationDto.of(item, postCommentReplyAssembler.convertToDto(postCommentReply), actor));

            } else if (item.getTargetEntityType() == AnswerCommentNotification.EntityType.ANSWER_COMMENT) {
                AnswerComment answerComment = answerCommentMap.get(item.getTargetEntityId());
                notificationDtos.add(AnswerCommentNotificationDto.of(item, answerCommentAssembler.convertToDto(answerComment), actor));
            }
        });

        CursorPageData<AnswerCommentNotificationDto> res = CursorPageData
                .of(notificationDtos, answerCommentNotifications.isHasMore(),
                        answerCommentNotifications.getPageSize(), answerCommentNotifications.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("follow-notifications")
    public Response<CursorPageData<ParticipatorFollowNotificationDto>> getParticipatorFollowNotifications(
            @RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        CursorPage<ParticipatorFollowNotification> followNotifications =
                participatorFollowNotificationRepository
                        .findByFolloweeId(userId, cursor, DEAULT_PAGE_SIZE);
        List<Long> userIds = followNotifications.getResult()
                .stream().map(item -> item.getFollowerId()).collect(Collectors.toList());
        Map<Long, Participator> participatorMap =
                participatorRepository.findByIds(userIds)
                        .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
        List<ParticipatorFollowNotificationDto> notificationDtos =
                followNotifications.getResult().stream()
                        .map(item -> {
                            Participator participator = participatorMap.get(item.getFollowerId());
                            return ParticipatorFollowNotificationDto.of(item,
                                    participtorAssembler.convertToDto(participator));
                        }).collect(Collectors.toList());

        CursorPageData<ParticipatorFollowNotificationDto> res = CursorPageData
                .of(notificationDtos, followNotifications.isHasMore(),
                        followNotifications.getPageSize(), followNotifications.getNextCursor());
        return Response.ok(res);
    }

    // TODO: make this parallel
    @GetMapping("vote-notifications")
    public Response<CursorPageData<VoteNotificationDto>> getVoteNotifications(
            @RequestParam(value = "cursor", required = false) Long cursor) {
        Long userId = LoginUtils.currentUserId();
        CursorPage<UpvoteNotification> upvoteNotifications =
                upvoteNotificationRepository
                        .findByOwnerId(userId, cursor, DEAULT_PAGE_SIZE);

        List<Long> postIds = upvoteNotifications.getResult()
                .stream()
                .filter(item -> item.getTargetEntityType() == UpvoteNotification.EntityType.POST)
                .map(UpvoteNotification::getTargetEntityId)
                .collect(Collectors.toList());

        List<Long> questionIds = upvoteNotifications.getResult()
                .stream()
                .filter(item -> item.getTargetEntityType() == UpvoteNotification.EntityType.QUESTION)
                .map(UpvoteNotification::getTargetEntityId)
                .collect(Collectors.toList());

        List<Long> answerIds = upvoteNotifications.getResult()
                .stream()
                .filter(item -> item.getTargetEntityType() == UpvoteNotification.EntityType.ANSWER)
                .map(UpvoteNotification::getTargetEntityId)
                .collect(Collectors.toList());

        Map<Long, Answer> answerMap = answerRepository.findByIds(answerIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
        Map<Long, Question> questionMap = questionRepository.findByIds(questionIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
        Map<Long, Post> postMap = postRepository.findByIds(postIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> item));

        List<Long> actorIds = upvoteNotifications.getResult()
                .stream().map(UpvoteNotification::getActorId).collect(Collectors.toList());
        Map<Long, ParticipatorDto> actorMap = participatorRepository.findByIds(actorIds)
                .stream().collect(Collectors.toMap(item -> item.getId(), item -> participtorAssembler.convertToDto(item)));

        List<VoteNotificationDto> notificationDtos = new ArrayList<>();
        upvoteNotifications.getResult()
                .stream().forEach(item -> {
            ParticipatorDto actor = actorMap.get(item.getActorId());
            if (item.getTargetEntityType() == UpvoteNotification.EntityType.ANSWER) {
                Answer answer = answerMap.get(item.getTargetEntityId());
                notificationDtos.add(VoteNotificationDto.of(item, answerAssembler.convertToDto(answer), actor));

            } else if (item.getTargetEntityType() == UpvoteNotification.EntityType.QUESTION) {
                Question postComment = questionMap.get(item.getTargetEntityId());
                notificationDtos.add(VoteNotificationDto.of(item, questionAssembler.convertToDto(postComment), actor));

            } else if (item.getTargetEntityType() == UpvoteNotification.EntityType.POST) {
                Post post = postMap.get(item.getTargetEntityId());
                notificationDtos.add(VoteNotificationDto.of(item, postAssembler.convertToDto(post), actor));
            }
        });

        CursorPageData<VoteNotificationDto> res = CursorPageData
                .of(notificationDtos, upvoteNotifications.isHasMore(),
                        upvoteNotifications.getPageSize(), upvoteNotifications.getNextCursor());
        return Response.ok(res);
    }

    @PutMapping("answer-comment-notifications")
    public Response<ReadNotificationResult> readAllAnswerCommentNotification() {
        Long userId = LoginUtils.currentUserId();
        notificationApplicationService.readAllanswerCommentNotifications(userId);

        ReadNotificationResult res =
                ReadNotificationResult.builder()
                        .receiverId(userId)
                        .build();
        return Response.ok(res);
    }

    @PutMapping("follow-notifications")
    public Response<ReadNotificationResult> readAllParticipatorFollowNotification() {
        Long userId = LoginUtils.currentUserId();
        notificationApplicationService.readAllParticipatorFollowNotifications(userId);

        ReadNotificationResult res =
                ReadNotificationResult.builder()
                        .receiverId(userId)
                        .build();
        return Response.ok(res);
    }

    @PutMapping("vote-notifications")
    public Response<ReadNotificationResult> readAllVotewNotification() {
        Long userId = LoginUtils.currentUserId();
        notificationApplicationService.readAllVoteNotificationServices(userId);

        ReadNotificationResult res =
                ReadNotificationResult.builder()
                        .receiverId(userId)
                        .build();
        return Response.ok(res);
    }

}
