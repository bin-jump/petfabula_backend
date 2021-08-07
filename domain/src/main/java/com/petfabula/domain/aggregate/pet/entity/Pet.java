package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.domain.ConcurrentEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "pet")
public class Pet extends ConcurrentEntity {

    public Pet(Long id, Long feederId, String name, String photo, LocalDate birthday,
               LocalDate arrivalDay, Gender gender, Double weight,
               String category, String breed) {
        setId(id);
        this.feederId = feederId;
        setName(name);
        this.photo = photo;
        setBirthday(birthday);
        this.arrivalDay = arrivalDay;
        this.gender = gender;
        this.weight = weight;
        this.category = category;
        this.breed = breed;
        this.feedRecordCount = 0;
        this.disorderRecordCount = 0;
        this.eventRecordCount = 0;
        this.weightRecordCount = 0;
        this.medicalRecordCount = 0;
    }

    @Column(name = "feeder_id")
    private Long feederId;

    @Column(name = "name", unique = true, nullable = false, length = 32)
    private String name;

    @Column(name = "photo")
    private String photo;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "arrivalday")
    private LocalDate arrivalDay;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "category")
    private String category;

    @Column(name = "breed")
    private String breed;

    @Column(name = "register_number")
    private String registerNumber;

    @Column(name = "bio")
    private String bio;

    @Column(name = "feed_record_count", nullable = false)
    private Integer feedRecordCount;

    @Column(name = "disorder_record_count", nullable = false)
    private Integer disorderRecordCount;

    @Column(name = "medical_record_count", nullable = false)
    private Integer medicalRecordCount;

    @Column(name = "weight_record_count", nullable = false)
    private Integer weightRecordCount;

    @Column(name = "event_record_count", nullable = false)
    private Integer eventRecordCount;

    public void setName(String name) {
        EntityValidationUtils.validPetName("name", name);
        this.name = name;
    }

    public void setBirthday(LocalDate birthday) {
        EntityValidationUtils.validBirthday("birthday", birthday);
        this.birthday = birthday;
    }

    public void setFeedRecordCount(Integer feedRecordCount) {
        this.feedRecordCount = feedRecordCount;
    }

    public void setDisorderRecordCount(Integer disorderRecordCount) {
        this.disorderRecordCount = disorderRecordCount;
    }

    public void setWeightRecordCount(Integer weightRecordCount) {
        this.weightRecordCount = weightRecordCount;
    }

    public void setEventRecordCount(Integer eventRecordCount) {
        this.eventRecordCount = eventRecordCount;
    }

    public void setMedicalRecordCount(Integer medicalRecordCount) {
        this.medicalRecordCount = medicalRecordCount;
    }

    public enum Gender {
        MALE, FEMALE, UNSET
    }
}
