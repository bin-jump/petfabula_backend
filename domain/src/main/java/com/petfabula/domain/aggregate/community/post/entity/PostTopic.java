package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.common.domain.AutoIdEntity;
import com.petfabula.domain.common.domain.GeneralEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post_topic")
public class PostTopic extends GeneralEntity {

    public PostTopic(Long id, String title, Long topicCategoryId, String category) {
        setId(id);
        this.title = title;
        this.topicCategoryId = topicCategoryId;
        this.topicCategoryTitle = category;
        this.petCategory = "";
    }

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "topic_category_id", nullable = false)
    private Long topicCategoryId;

    @Column(name = "topic_category_title", nullable = false)
    private String topicCategoryTitle;

    @Column(name = "pet_category", nullable = false)
    private String petCategory;
}
