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

@NamedEntityGraph(name = "medicalRecord.all",
        attributeNodes = {@NamedAttributeNode("images")}
)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "pet_medical_record",
        indexes = {@Index(columnList = "pet_id, date_time")})
public class MedicalRecord extends GeneralEntity {

    public MedicalRecord(Long id, Long petId, String hospitalName, String symptom,
                         String diagnosis, String treatment, Instant dateTime, String note) {
        setId(id);
        this.petId = petId;
        setHospitalName(hospitalName);
        setSymptom(symptom);
        setDiagnosis(diagnosis);
        setTreatment(treatment);
        setDateTime(dateTime);
        setNote(note);
    }

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "Symptom", length = 500, nullable = false)
    private String symptom;

    @Column(name = "diagnosis", length = 500)
    private String diagnosis;

    @Column(name = "treatment", length = 500)
    private String treatment;

    @Column(name = "date_time", nullable = false)
    private Instant dateTime;

    @Column(name = "note")
    private String note;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="medical_record_id", foreignKey = @ForeignKey(name = "none"))
    private List<MedicalRecordImage> images = new ArrayList<>();

    public void setHospitalName(String hospitalName) {
        EntityValidationUtils.validStringLength("note", note, 0, 30);
        this.hospitalName = hospitalName;
    }

    public void setSymptom(String symptom) {
        EntityValidationUtils.validStringLength("note", note, 0, 240);
        this.symptom = symptom;
    }

    public void setDiagnosis(String diagnosis) {
        EntityValidationUtils.validStringLength("note", note, 0, 240);
        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {
        EntityValidationUtils.validStringLength("note", note, 0, 240);
        this.treatment = treatment;
    }

    public void setDateTime(Instant dateTime) {
        EntityValidationUtils.validRecordDate("dateTime", dateTime);
        this.dateTime = dateTime;
    }

    public void setNote(String note) {
        EntityValidationUtils.validStringLength("note", note, 0, 200);
        this.note = note;
    }

    public void addImage(MedicalRecordImage image) {
        if (images.size() == 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        images.add(image);
    }

    public void removeImage(Long id) {
        MedicalRecordImage image = images.stream().
                filter(p -> p.getId().equals(id)).
                findFirst().orElse(null);
        if (image != null) {
            images.removeIf(x -> x.getId().equals(id));
        }
    }
}
