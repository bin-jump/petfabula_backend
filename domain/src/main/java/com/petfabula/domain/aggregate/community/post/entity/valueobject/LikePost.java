package com.petfabula.domain.aggregate.community.post.entity.valueobject;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePostId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

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
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @MapsId("participatorId")
    private Participator participator;

    @ManyToOne()
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @MapsId("postId")
    private Post post;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();
}
