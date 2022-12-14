package com.petfabula.domain.aggregate.community.participator.entity;

import com.petfabula.domain.common.domain.ConcurrentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "participator",
        indexes = {@Index(name = "name_index",  columnList="name")})
public class Participator extends ConcurrentEntity {

    public Participator(Long id, String name, String photo) {
        setId(id);
        setName(name);
        setPhoto(photo);
        setBio("");
        setGender(null);
        petCount = 0;
        postCount = 0;
        followerCount = 0;
        followedCount = 0;
        questionCount = 0;
        answerCount = 0;
        collectCount = 0;
    }

    @Column(name = "name", unique = true, nullable = false, length = 32)
    private String name;

    @Column(name = "photo", unique = true)
    private String photo;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "gender", length = 16)
    private String gender;

    @Column(name = "pet_count", nullable = false)
    private Integer petCount;

    @Column(name = "post_count", nullable = false)
    private Integer postCount;

    @Column(name = "follower_count", nullable = false)
    private Integer followerCount;

    @Column(name = "followed_count", nullable = false)
    private Integer followedCount;

    @Column(name = "question_count", nullable = false)
    private Integer questionCount;

    @Column(name = "answer_count", nullable = false)
    private Integer answerCount;

    @Column(name = "collect_count", nullable = false)
    private Integer collectCount;

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPetCount(Integer petCount) {
        this.petCount = petCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public void setFollowedCount(Integer followedCount) {
        this.followedCount = followedCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }
}
