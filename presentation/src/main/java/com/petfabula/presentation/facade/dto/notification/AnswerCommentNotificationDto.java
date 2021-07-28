package com.petfabula.presentation.facade.dto.notification;

import com.petfabula.domain.aggregate.notification.entity.AnswerCommentNotification;
import com.petfabula.presentation.facade.dto.ImageDto;
import com.petfabula.presentation.facade.dto.community.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerCommentNotificationDto {

    private Long id;

    private Object targetEntity;

//    private Long rootId;

    private Long entityId;

    private String content;

    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();

    private ParticipatorDto actor;

    private AnswerCommentNotification.EntityType targetEntityType;

    private AnswerCommentNotification.ActionType actionType;

    private Long createdDate;

    public static AnswerCommentNotificationDto of(AnswerCommentNotification notification, QuestionDto questionDto, ParticipatorDto actor) {
        AnswerCommentNotificationDto res = make(notification, questionDto, actor);
        res.setContent(questionDto.getTitle());
        res.setImages(questionDto.getImages());
        return res;
    }

    public static AnswerCommentNotificationDto of(AnswerCommentNotification notification, PostDto postDto, ParticipatorDto actor) {
        AnswerCommentNotificationDto res = make(notification, postDto, actor);
        res.setContent(postDto.getContent());
        res.setImages(postDto.getImages());
        return res;
    }

    public static AnswerCommentNotificationDto of(AnswerCommentNotification notification, AnswerDto answerDto, ParticipatorDto actor) {
        AnswerCommentNotificationDto res = make(notification, answerDto, actor);
        res.setContent(answerDto.getContent());
        res.setImages(answerDto.getImages());
        return res;
    }

    public static AnswerCommentNotificationDto of(AnswerCommentNotification notification, PostCommentDto postCommentDto, ParticipatorDto actor) {
        AnswerCommentNotificationDto res = make(notification, postCommentDto, actor);
        res.setContent(postCommentDto.getContent());
        return res;
    }

    public static AnswerCommentNotificationDto of(AnswerCommentNotification notification, PostCommentReplyDto postCommentReplyDto, ParticipatorDto actor) {
        AnswerCommentNotificationDto res = make(notification, postCommentReplyDto, actor);
        res.setContent(postCommentReplyDto.getContent());
        return res;
    }

    public static AnswerCommentNotificationDto of(AnswerCommentNotification notification, AnswerCommentDto answerCommentDto, ParticipatorDto actor) {
        AnswerCommentNotificationDto res = make(notification, answerCommentDto, actor);
        res.setContent(answerCommentDto.getContent());
        return res;
    }

    private static AnswerCommentNotificationDto make(AnswerCommentNotification notification, Object targetEntity, ParticipatorDto actor) {
        AnswerCommentNotificationDto res = AnswerCommentNotificationDto.builder()
                .id(notification.getId())
                .entityId(notification.getEntityId())
                .targetEntityType(notification.getTargetEntityType())
                .actionType(notification.getActionType())
                .createdDate(notification.getCreatedDate().toEpochMilli())
                .targetEntity(targetEntity)
                .actor(actor)
                .build();
        return res;
    }


}
