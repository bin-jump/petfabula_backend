package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.domain.ConcurrentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "feeder")
public class Feeder extends ConcurrentEntity {

    public Feeder(Long id, String name, String photo) {
        setId(id);
        this.name = name;
        this.photo = photo;
        this.petCount = 0;

        this.showMedicalRecord = true;
        this.showDisorderRecord = true;
        this.showPetEventRecord = false;
        this.showFeedRecord = false;
        this.showWeightRecord = false;
    }

    @Column(name = "name", unique = true, nullable = false, length = 32)
    private String name;

    @Column(name = "photo", unique = true)
    private String photo;

    @Column(name = "pet_count", nullable = false)
    private Integer petCount;

    @Column(name = "show_disorder_record", nullable = false)
    private boolean showDisorderRecord;

    @Column(name = "show_feed_record", nullable = false)
    private boolean showFeedRecord;

    @Column(name = "show_medical_record", nullable = false)
    private boolean showMedicalRecord;

    @Column(name = "show_weight_record", nullable = false)
    private boolean showWeightRecord;

    @Column(name = "show_pet_event_record", nullable = false)
    private boolean showPetEventRecord;

    public void setPetCount(Integer petCount) {
        this.petCount = petCount;
    }

    public void setShowDisorderRecord(boolean showDisorderRecord) {
        this.showDisorderRecord = showDisorderRecord;
    }

    public void setShowFeedRecord(boolean showFeedRecord) {
        this.showFeedRecord = showFeedRecord;
    }

    public void setShowMedicalRecord(boolean showMedicalRecord) {
        this.showMedicalRecord = showMedicalRecord;
    }

    public void setShowWeightRecord(boolean showWeightRecord) {
        this.showWeightRecord = showWeightRecord;
    }

    public void setShowPetEventRecord(boolean showEventRecord) {
        this.showPetEventRecord = showEventRecord;
    }
}
