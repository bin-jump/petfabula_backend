package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.common.domain.GeneralEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post_image",
        indexes = {@Index(name = "post_id_index",  columnList="post_id")})
public class PostImage extends EntityBase {

    public PostImage(Long id, String uri, Long postId, Long petId, Integer w, Integer h) {
        setId(id);
        this.petId = petId;
        this.url = uri;
        this.width = w;
        this.height = h;
        this.postId = postId;
    }

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;

    public void setPetId(Long petId) {
        this.petId = petId;
    }
}
