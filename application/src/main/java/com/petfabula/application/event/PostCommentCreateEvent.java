package com.petfabula.application.event;

import com.petfabula.domain.aggregate.community.post.entity.PostComment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentCreateEvent {

    private PostComment postComment;
}
