package com.petfabula.domain.aggregate.community.question.entity;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.ConcurrentEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import com.petfabula.domain.exception.InvalidOperationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "answer")
public class Answer extends ConcurrentEntity {

    public Answer(Long id, Participator participator, Long questionId, String content) {
        setId(id);
        setContent(content);
        this.participator = participator;
        this.questionId = questionId;
        this.upvoteCount = 0;
        this.viewCount = 0;
        this.commentCount = 0;
    }

    @Column(name = "content", nullable = false, length = 10000)
    private String content;

    @Column(name = "upvote_count", nullable = false)
    private Integer upvoteCount;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    // this will create Cartesian product
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "none"))
//    private Question question;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id", foreignKey = @ForeignKey(name = "none"))
    private Participator participator;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "answer_id", foreignKey = @ForeignKey(name = "none"))
    private List<AnswerImage> images = new ArrayList<>();

    public void setContent(String content) {
        EntityValidationUtils.validStringLength("content", content, 0, 10000);
        this.content = content;
    }

    public void setUpvoteCount(Integer upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public void addImage(AnswerImage image) {
        if (images.size() == 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        images.add(image);
    }
}
