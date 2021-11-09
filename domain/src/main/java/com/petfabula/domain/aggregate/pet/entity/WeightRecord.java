package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import com.petfabula.domain.exception.InvalidValueException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.Instant;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "pet_weight_record",
        indexes = {@Index(columnList = "pet_id, date_time, delete_at", unique = true)})
public class WeightRecord extends GeneralEntity {

    public WeightRecord(Long id, Long petId, Instant dateTime, Integer weight) {
        setId(id);
        this.petId = petId;
        setDateTime(dateTime);
        setWeight(weight);
    }

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "date_time", nullable = false)
    private Instant dateTime;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    public void setWeight(Integer weight) {
        if (weight <= 0 || weight > 100000) {
            throw new InvalidValueException("weight", CommonMessageKeys.CANNOT_PROCEED);
        }
        this.weight = weight;
    }

    public void setDateTime(Instant date) {
        EntityValidationUtils.validRecordDate("date", date);
        this.dateTime = date;
    }
}
