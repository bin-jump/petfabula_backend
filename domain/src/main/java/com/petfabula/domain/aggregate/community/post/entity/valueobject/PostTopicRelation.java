package com.petfabula.domain.aggregate.community.post.entity.valueobject;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_topic_relation",
        indexes = {@Index(name = "post_topic_index",  columnList="post_topic_id")})
public class PostTopicRelation {

    public PostTopicRelation(PostTopic postTopic, Post post) {
        this.postTopic = postTopic;
        this.post = post;
    }

    // just use postId as primary key,
    // performance benefit (create by post id order)
    @Id
    @Column(name = "post_id", insertable = false, updatable = false)
    private Long postId;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name = "none"))
    private Post post;

    @OneToOne
    @JoinColumn(name = "post_topic_id", nullable = false,
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private PostTopic postTopic;

}
