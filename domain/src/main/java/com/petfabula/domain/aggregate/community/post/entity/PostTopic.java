package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicCategory;
import com.petfabula.domain.common.domain.AutoIdEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post_topic")
public class PostTopic extends AutoIdEntity {

    public PostTopic(String title, PostTopicCategory topicCategory) {
        this.title = title;
        this.topicCategory = topicCategory;
    }

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "none"))
    private PostTopicCategory topicCategory;
}
