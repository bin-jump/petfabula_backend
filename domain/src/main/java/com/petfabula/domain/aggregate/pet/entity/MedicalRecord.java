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
@Table(name = "medical_record",
        indexes = {@Index(columnList = "pet_id, date")})
public class MedicalRecord extends GeneralEntity {

    public MedicalRecord(Long id, Long petId, String hospitalName, String symptom,
                         String diagnosis, String treatment, Instant date, String note) {
        setId(id);
        this.petId = petId;
        this.hospitalName = hospitalName;
        this.symptom = symptom;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.date = date;
        this.note = note;
    }

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "Symptom")
    private String symptom;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "note")
    private String note;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="medical_record_id")
    private List<MedicalRecordImage> images = new ArrayList<>();

    public void addImage(MedicalRecordImage image) {
        if (images.size() == 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        images.add(image);
    }
}
