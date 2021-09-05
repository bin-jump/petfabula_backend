package com.petfabula.presentation.facade.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotifySettingDto {

    private boolean receiveAnswerComment;

    private boolean receiveFollow;

    private boolean receiveUpvote;
}
