package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.common.domain.AutoIdEntity;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
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

    @Column(name = "pet_category")
    private String petCategory;

    public void setTitle(String title) {
        EntityValidationUtils.validStringLength("title", title, 2, 20);
        this.title = title;
    }
}
