package com.petfabula.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipatorFollowEvent {

    private Long followerId;

    private Long followeeId;
}
