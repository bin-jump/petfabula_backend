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
@Table(name = "question")
public class Question extends ConcurrentEntity {

    public Question(Long id, Participator participator, String title, String content) {
        setId(id);
        setTitle(title);
        setContent(content);
        this.participator = participator;
        this.answerCount = 0;
        this.upvoteCount = 0;
        this.viewCount = 0;
    }

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "answer_count", nullable = false)
    private Integer answerCount;

    @Column(name = "upvote_count", nullable = false)
    private Integer upvoteCount;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "none"))
    private List<QuestionImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id", foreignKey = @ForeignKey(name = "none"))
    private Participator participator;

    public void setTitle(String title) {
        EntityValidationUtils.validStringLendth("title", title, 3, 50);
        this.title = title;
    }

    public void setContent(String content) {
        EntityValidationUtils.validStringLendth("content", content, 0, 1000);
        this.content = content;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public void setUpvoteCount(Integer upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public void addImage(QuestionImage image) {
        if (images.size() == 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        images.add(image);
    }
}
