package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "pet_medical_record_image",
        indexes = {@Index(name = "medical_record_id_index",  columnList="medical_record_id")})
public class MedicalRecordImage extends EntityBase {

    public MedicalRecordImage(Long id, Long medicalRecordId, String url,
                              Integer width, Integer height) {
        setId(id);
        this.medicalRecordId = medicalRecordId;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    @Column(name = "medical_record_id", nullable = false)
    private Long medicalRecordId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="pet_event_record_id")
    private List<PetEventRecordImage> images = new ArrayList<>();
}
