package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.common.domain.AutoIdEntity;
import com.petfabula.domain.common.domain.GeneralEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post_topic_category")
public class PostTopicCategory extends GeneralEntity {

    public PostTopicCategory(Long id, String title) {
        setId(id);
        this.title = title;
        this.petCategory = "";
    }

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "pet_category", nullable = false)
    private String petCategory;
}
