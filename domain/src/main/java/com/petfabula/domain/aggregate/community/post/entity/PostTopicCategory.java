package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.common.domain.AutoIdEntity;
import com.petfabula.domain.common.domain.GeneralEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraphs({
        @NamedEntityGraph(name = "postTopicCategory.all",
                attributeNodes = {@NamedAttributeNode("topics")}),
        @NamedEntityGraph(name = "postTopicCategory.bare")
})
@NoArgsConstructor
@Getter
@Entity
@Table(name = "post_topic_category")
public class PostTopicCategory extends GeneralEntity {

    public PostTopicCategory(Long id, String title) {
        setId(id);
        this.title = title;
    }

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PostTopic> topics = new ArrayList<>();

    public void addTopic(PostTopic postTopic) {
        topics.add(postTopic);
    }
}
