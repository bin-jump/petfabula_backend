package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.exception.InvalidOperationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "pet_disorder_record",
        indexes = {@Index(columnList = "pet_id, date")})
public class DisorderRecord extends GeneralEntity {

    public DisorderRecord(Long id, Long petId, Instant date, String disorderType, String note) {
        setId(id);
        this.petId = petId;
        this.date = date;
        this.disorderType = disorderType;
        this.note = note;
    }

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "disorder_type", nullable = false)
    private String disorderType;

    @Column(name = "note")
    private String note;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="disorder_record_id")
    private List<DisorderRecordImage> images = new ArrayList<>();

    public void addImage(DisorderRecordImage image) {
        if (images.size() == 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        images.add(image);
    }
}
