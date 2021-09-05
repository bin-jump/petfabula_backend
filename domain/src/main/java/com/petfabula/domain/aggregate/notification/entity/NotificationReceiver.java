package com.petfabula.domain.aggregate.notification.entity;

import com.petfabula.domain.common.domain.ConcurrentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.Instant;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "notification_receiver")
public class NotificationReceiver extends ConcurrentEntity {

    public NotificationReceiver(Long id) {
        setId(id);
        this.answerCommentUnreadCount = 0;
        this.participatorFollowUnreadCount = 0;
        this.upvoteUnreadCount = 0;
        this.systemLastReadTime = Instant.now();
        this.receiveAnswerComment = true;
        this.receiveFollow = true;
        this.receiveUpvote = true;
    }

    @Column(name = "answer_comment_unread_count", nullable = false)
    private Integer answerCommentUnreadCount;

    @Column(name = "participator_follow_unread_count", nullable = false)
    private Integer participatorFollowUnreadCount;

    @Column(name = "vote_unread_count", nullable = false)
    private Integer upvoteUnreadCount;

    @Column(name = "system_last_read_time", nullable = false)
    private Instant systemLastReadTime;

    @Column(name = "receive_answer_comment", nullable = false)
    private boolean receiveAnswerComment;

    @Column(name = "receive_follow", nullable = false)
    private boolean receiveFollow;

    @Column(name = "receive_upvote", nullable = false)
    private boolean receiveUpvote;

    public void setReceiveAnswerComment(boolean receiveAnswerComment) {
        this.receiveAnswerComment = receiveAnswerComment;
    }

    public void setReceiveFollow(boolean receiveFollow) {
        this.receiveFollow = receiveFollow;
    }

    public void setReceiveUpvote(boolean receiveUpvote) {
        this.receiveUpvote = receiveUpvote;
    }
}
