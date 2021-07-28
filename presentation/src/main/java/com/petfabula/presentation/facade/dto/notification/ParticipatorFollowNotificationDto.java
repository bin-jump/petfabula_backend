package com.petfabula.presentation.facade.dto.notification;

import com.petfabula.domain.aggregate.notification.entity.ParticipatorFollowNotification;
import com.petfabula.presentation.facade.dto.community.ParticipatorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipatorFollowNotificationDto {

    public static ParticipatorFollowNotificationDto of(ParticipatorFollowNotification notification, ParticipatorDto follower) {
        ParticipatorFollowNotificationDto res = ParticipatorFollowNotificationDto.builder()
                .id(notification.getId())
                .follower(follower)
                .createdDate(notification.getCreatedDate().toEpochMilli())
                .build();

        return res;
    }

    private Long id;

    private ParticipatorDto follower;

    private Long createdDate;
}
