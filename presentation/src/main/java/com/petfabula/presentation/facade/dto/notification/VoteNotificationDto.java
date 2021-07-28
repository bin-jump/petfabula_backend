package com.petfabula.presentation.facade.dto.notification;

import com.petfabula.domain.aggregate.notification.entity.UpvoteNotification;
import com.petfabula.presentation.facade.dto.ImageDto;
import com.petfabula.presentation.facade.dto.community.AnswerDto;
import com.petfabula.presentation.facade.dto.community.ParticipatorDto;
import com.petfabula.presentation.facade.dto.community.PostDto;
import com.petfabula.presentation.facade.dto.community.QuestionDto;
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
public class VoteNotificationDto {

    private Long id;

    private Object targetEntity;

    private String content;

    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();

    private ParticipatorDto actor;

    private UpvoteNotification.EntityType targetEntityType;

    private UpvoteNotification.ActionType actionType;

    private Long createdDate;

    public static VoteNotificationDto of(UpvoteNotification notification, PostDto postDto, ParticipatorDto actor) {
        VoteNotificationDto res = make(notification, postDto, actor);
        res.setContent(postDto.getContent());
        res.setImages(postDto.getImages());

        return res;
    }

    public static VoteNotificationDto of(UpvoteNotification notification, QuestionDto questionDto, ParticipatorDto actor) {
        VoteNotificationDto res = make(notification, questionDto, actor);
        res.setContent(questionDto.getTitle());
        res.setImages(questionDto.getImages());

        return res;
    }

    public static VoteNotificationDto of(UpvoteNotification notification, AnswerDto answerDto, ParticipatorDto actor) {
        VoteNotificationDto res = make(notification, answerDto, actor);
        res.setContent(answerDto.getContent());
        res.setImages(answerDto.getImages());

        return res;
    }

    private static VoteNotificationDto make(UpvoteNotification notification, Object targetEntity, ParticipatorDto actor) {
        VoteNotificationDto res = VoteNotificationDto.builder()
                .id(notification.getId())
                .targetEntityType(notification.getTargetEntityType())
                .actionType(notification.getActionType())
                .createdDate(notification.getCreatedDate().toEpochMilli())
                .targetEntity(targetEntity)
                .actor(actor)
                .build();
        return res;
    }

}
