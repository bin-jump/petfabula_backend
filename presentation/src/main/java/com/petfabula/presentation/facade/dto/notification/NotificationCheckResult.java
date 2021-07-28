package com.petfabula.presentation.facade.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationCheckResult {

    private Long receiverId;

    private Integer answerCommentCount;

    private Integer followCount;

    private Integer voteCount;

    private boolean hasSystemNotificationUnread;
}
