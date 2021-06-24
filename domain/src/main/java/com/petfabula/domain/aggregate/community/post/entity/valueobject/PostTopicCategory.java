package com.petfabula.domain.aggregate.community.post.entity.valueobject;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.common.domain.AutoIdEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post_topic_category")
public class PostTopicCategory extends AutoIdEntity {

    public PostTopicCategory(String title) {
        this.title = title;
    }

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "topicCategory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PostTopic> topics = new ArrayList<>();

    public void addTopic(PostTopic postTopic) {
        topics.add(postTopic);
    }
}
