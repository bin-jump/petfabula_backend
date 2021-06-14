package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post_image")
public class PostImage extends EntityBase {

    public PostImage(String uri, Post post) {
        this.url = uri;
        setPost(post);
    }

    @Column(name = "url", unique = true)
    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public void setPost(Post post) {
        this.post = post;
    }
}
