package com.petfabula.domain.aggregate.community.entity;

import com.petfabula.domain.base.AutoIdEntity;
import com.petfabula.domain.base.ConcurrentEntity;
import com.petfabula.domain.base.EntityBase;
import com.petfabula.domain.base.GeneralEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post_topic")
public class PostTopic extends AutoIdEntity {

    public PostTopic(String title, String intro) {
        this.title = title;
        this.intro = intro;
    }

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "intro", nullable = false)
    private String intro;
}
