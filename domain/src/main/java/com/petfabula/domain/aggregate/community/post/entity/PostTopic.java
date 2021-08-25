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

    public PostTopic(Long id, String title, Long topicCategoryId) {
        setId(id);
        this.title = title;
        this.categoryId = topicCategoryId;
    }

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;
}
