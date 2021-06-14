package com.petfabula.domain.aggregate.community.post.entity.valueobject;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePostId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_like")
public class LikePost {

    public LikePost(Participator participator, Post post) {
        this.likePostId = new LikePostId(participator.getId(), post.getId());
        this.participator = participator;
        this.post = post;
    }

    @EmbeddedId
    private LikePostId likePostId;

    @ManyToOne()
    @MapsId("participatorId")
    private Participator participator;

    @ManyToOne()
    @MapsId("postId")
    private Post post;
}
