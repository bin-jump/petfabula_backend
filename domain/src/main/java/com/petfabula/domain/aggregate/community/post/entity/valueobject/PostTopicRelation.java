package com.petfabula.domain.aggregate.community.post.entity.valueobject;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_topic_relation",
        indexes = {@Index(name = "post_topic_index",  columnList="topic_id")})
public class PostTopicRelation extends EntityBase {

    public PostTopicRelation(Long id, Long postId, Long topicId, Long topicCategoryId) {
        setId(id);
        this.postId = postId;
        this.topicId = topicId;
        this.topicCategoryId = topicCategoryId;
    }

    @Column(name = "post_id", nullable = false, unique = true)
    private Long postId;

    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    @Column(name = "topic_category_id", nullable = false)
    private Long topicCategoryId;

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
