package com.petfabula.domain.aggregate.community.question.entity;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.ConcurrentEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import com.petfabula.domain.exception.InvalidOperationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NamedEntityGraph(name = "question.all",
        attributeNodes = {@NamedAttributeNode("participator"), @NamedAttributeNode("images")}
)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "question")
public class Question extends ConcurrentEntity {

    public Question(Long id, Participator participator, String title, String content, ParticipatorPet pet) {
        setId(id);
        setTitle(title);
        setContent(content);
        this.participator = participator;
        this.answerCount = 0;
        this.upvoteCount = 0;
        this.viewCount = 0;
        setRelatePet(pet);
    }

    @Column(name = "pet_id")
    private Long relatePetId;

    @Column(name = "pet_category")
    private String petCategory;

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "none"))
    @OrderBy
    private List<QuestionImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id", foreignKey = @ForeignKey(name = "none"))
    private Participator participator;

    public void setTitle(String title) {
        EntityValidationUtils.validStringLength("title", title, 3, 50);
        this.title = title;
    }

    public void setContent(String content) {
        EntityValidationUtils.validStringLength("content", content, 0, 1000);
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

    public void setRelatePet(ParticipatorPet pet) {
        if (pet != null) {
            this.relatePetId = pet.getId();
            this.petCategory = pet.getCategory();
        }
    }

    public void addImage(QuestionImage image) {
        if (images.size() == 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        images.add(image);
    }

    public void removeImage(Long id) {
        images.removeIf(x -> x.getId().equals(id));
    }
}
