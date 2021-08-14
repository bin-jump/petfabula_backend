package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.post.PostMessageKeys;
import com.petfabula.domain.common.domain.ConcurrentEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import com.petfabula.domain.exception.InvalidOperationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(name = "post.all",
        attributeNodes = {@NamedAttributeNode("participator"), @NamedAttributeNode("images")}
)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "post")
public class Post extends ConcurrentEntity {

    public Post(Long id, Participator participator, String content, ParticipatorPet pet) {
        setId(id);
        setContent(content);
        this.participator = participator;
        this.likeCount = 0;
        this.commentCount = 0;
        this.collectCount = 0;
        this.viewCount = 0;
        this.showAll = true;
        if (pet != null) {
            this.relatePetId = pet.getId();
            this.petCategory = pet.getPetCategory();
        }
    }

    @Column(name = "pet_id")
    private Long relatePetId;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    @Column(name = "collect_count", nullable = false)
    private Integer collectCount;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @Column(name = "show_all", nullable = false)
    private Boolean showAll;

    @Column(name = "pet_category")
    private String petCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id", foreignKey = @ForeignKey(name = "none"))
    private Participator participator;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @OrderBy
    private List<PostImage> images = new ArrayList<>();

    public void setContent(String content) {
        EntityValidationUtils.validStringLength("content", content, 0, 5000);
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

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public void addImage(PostImage image) {
        if (images.size() == 6) {
            throw new InvalidOperationException(PostMessageKeys.TOO_MANY_POST_IMAGE);
        }
        image.setPost(this);
        images.add(image);
    }
}
