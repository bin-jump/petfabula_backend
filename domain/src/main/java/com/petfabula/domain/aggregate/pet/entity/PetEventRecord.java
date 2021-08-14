package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
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
@Table(name = "pet_event_record",
        indexes = {@Index(columnList = "pet_id, date_time")})
public class PetEventRecord extends GeneralEntity {

    public PetEventRecord(Long id, Long petId, Instant dateTime, String eventType, String content) {
        setId(id);
        this.petId = petId;
        this.dateTime = dateTime;
        this.eventType = eventType;
        setContent(content);
    }

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "date_time", nullable = false)
    private Instant dateTime;

    @Column(name = "disorder_type", nullable = false)
    private String eventType;

    @Column(name = "content", length = 500)
    private String content;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="pet_event_record_id")
    private List<PetEventRecordImage> images = new ArrayList<>();

    public void addImage(PetEventRecordImage image) {
        if (images.size() == 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        images.add(image);
    }

    public void setContent(String content) {
        EntityValidationUtils.validStringLength("content", content, 0, 240);
        this.content = content;
    }
}
