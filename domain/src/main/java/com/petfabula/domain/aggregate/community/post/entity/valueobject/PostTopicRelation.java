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
        this.postTopicRelationId =
                new PostTopicRelationId(post.getId(), postTopic.getId());
        this.postTopic = postTopic;
        this.post = post;
        this.topicCategoryId = postTopic.getTopicCategory().getId();
    }

    @EmbeddedId
    private PostTopicRelationId postTopicRelationId;

    @Column(name = "topic_category_id", nullable = false)
    private Long topicCategoryId;

    @ManyToOne()
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @MapsId("postId")
    private Post post;

    @ManyToOne()
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @MapsId("postTopicId")
    private PostTopic postTopic;
}
