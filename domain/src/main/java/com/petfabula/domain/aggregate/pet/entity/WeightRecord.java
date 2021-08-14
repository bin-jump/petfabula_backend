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
        indexes = {@Index(columnList = "pet_id, date")})
public class WeightRecord extends GeneralEntity {

    public WeightRecord(Long id, Long petId, Instant date, Double weight) {
        setId(id);
        this.petId = petId;
        this.date = date;
        this.weight = weight;
    }

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "weight", nullable = false)
    private Double weight;

    public void setWeight(Double weight) {
        if (weight <= 0 || weight > 100000) {
            throw new InvalidValueException("weight", CommonMessageKeys.CANNOT_PROCEED);
        }
        this.weight = weight;
    }

    public void setDate(Instant date) {
        EntityValidationUtils.validRecordDate("date", date);
        this.date = date;
    }
}
