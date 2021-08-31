package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "disorder_record_image",
        indexes = {@Index(name = "disorder_record_id_index",  columnList="disorder_record_id")})
public class DisorderRecordImage extends EntityBase {

    public DisorderRecordImage(Long id, Long disorderRecordId, String url,
                               Integer width, Integer height) {
        setId(id);
        this.disorderRecordId = disorderRecordId;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    @Column(name = "disorder_record_id")
    private Long disorderRecordId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;
}
