package com.petfabula.domain.aggregate.community.entity;

import com.petfabula.domain.aggregate.community.error.PostMessageKeys;
import com.petfabula.domain.base.ConcurrentEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import com.petfabula.domain.exception.InvalidOperationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post")
public class Post extends ConcurrentEntity {

    public Post(Long id, Participator participator, String content, Long relatePetId) {
        setId(id);
        setContent(content);
        this.participator = participator;
        this.likeCount = 0;
        this.commentCount = 0;
        this.relatePetId = relatePetId;
    }

    @Column(name = "pet_id")
    private Long relatePetId;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id")
    private Participator participator;

    @OneToMany(mappedBy = "post", cascade=CascadeType.ALL,
            fetch = FetchType.EAGER,orphanRemoval = true)
    private Set<PostImage> images = new HashSet<>();

    public void setContent(String content) {
        EntityValidationUtils.validStringLendth("content", content, 0, 5000);
        this.content = content;
    }

    public void setRelatePetId(Long relatePetId) {
        this.relatePetId = relatePetId;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public void addImage(PostImage image) {
        if (images.size() == 6) {
            throw new InvalidOperationException(PostMessageKeys.TOO_MANY_POST_IMAGE);
        }
        image.setPost(this);
        images.add(image);
    }
}
