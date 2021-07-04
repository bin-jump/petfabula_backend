package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post_image",
        indexes = {@Index(name = "post_id_index",  columnList="post_id")})
public class PostImage extends EntityBase {

    public PostImage(Long id, String uri, Post post, Integer w, Integer h) {
        setId(id);
        this.url = uri;
        this.width = w;
        this.height = h;
        setPost(post);
    }

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "none"))
    private Post post;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;

    public void setPost(Post post) {
        this.post = post;
    }
}
