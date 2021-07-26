package com.petfabula.domain.aggregate.community.post.entity.valueobject;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_like", uniqueConstraints={
        @UniqueConstraint(columnNames = {"participator_id", "post_id"})})
public class LikePost extends EntityBase {

    public LikePost(Long id, Participator participator, Post post) {
        setId(id);
        this.participatorId = participator.getId();
        this.postId = post.getId();
    }

    @Column(name = "participator_id")
    private Long participatorId;

    @Column(name = "post_id")
    private Long postId;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();
}
