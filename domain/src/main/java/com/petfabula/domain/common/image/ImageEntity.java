package com.petfabula.domain.common.image;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass
public abstract class ImageEntity extends EntityBase {

    @Column(name = "url", nullable = false)
    protected String url;

    @Column(name = "width", nullable = false)
    protected Integer width;

    @Column(name = "height", nullable = false)
    protected Integer height;
}
