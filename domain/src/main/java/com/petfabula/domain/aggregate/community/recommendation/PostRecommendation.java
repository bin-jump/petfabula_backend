package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.common.domain.GeneralEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NamedEntityGraph(name = "postRecommendation.bare")
@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_recommendation")
public class PostRecommendation extends GeneralEntity {

    public PostRecommendation(Long id, Long postId) {
        setId(id);
        this.postId = postId;
    }

    @Column(name = "post_id")
    private Long postId;
}
