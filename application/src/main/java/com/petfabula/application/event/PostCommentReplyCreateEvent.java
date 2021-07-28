package com.petfabula.application.event;

import com.petfabula.domain.aggregate.community.post.entity.PostCommentReply;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentReplyCreateEvent {

    private PostCommentReply postCommentReply;
}
