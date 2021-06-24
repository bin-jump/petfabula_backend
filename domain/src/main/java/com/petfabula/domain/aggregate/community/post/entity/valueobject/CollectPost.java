package com.petfabula.domain.aggregate.community.post.entity.valueobject;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_collect")
public class CollectPost {

    public CollectPost(Participator participator, Post post) {
        this.collectPostId = new CollectPostId(participator.getId(), post.getId());
        this.participator = participator;
        this.post = post;
    }

    @EmbeddedId
    private CollectPostId collectPostId;

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
