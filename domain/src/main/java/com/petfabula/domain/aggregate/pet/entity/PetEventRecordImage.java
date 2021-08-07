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
@Table(name = "pet_event_record_image",
        indexes = {@Index(name = "pet_event_record_id_index",  columnList="pet_event_record_id")})
public class PetEventRecordImage extends EntityBase {

    public PetEventRecordImage(Long id, Long petEventRecordId, String url, Integer width, Integer height) {
        setId(id);
        this.petEventRecordId = petEventRecordId;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    @Column(name = "pet_event_record_id", nullable = false)
    private Long petEventRecordId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;
}
