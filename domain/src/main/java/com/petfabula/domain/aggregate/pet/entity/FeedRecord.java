package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import com.petfabula.domain.common.validation.MessageKey;
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
@Table(name = "pet_feed_record",
        indexes = {@Index(columnList = "pet_id, date_time, delete_at", unique = true)})
public class FeedRecord extends GeneralEntity {

    public FeedRecord(Long id, Long petId, Instant dateTime, String foodContent, Integer amount, String note) {
        setId(id);
        this.petId = petId;
        setDateTime(dateTime);
        setFoodContent(foodContent);
        setAmount(amount);
        setNote(note);
    }

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "date_time", nullable = false)
    private Instant dateTime;

    @Column(name = "food_content", nullable = false)
    private String foodContent;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "note")
    private String note;

    public void setAmount(Integer amount) {
        if (amount <= 0 || amount > 100000) {
            throw new InvalidValueException("amount", CommonMessageKeys.CANNOT_PROCEED);
        }
        this.amount = amount;
    }

    public void setFoodContent(String foodContent) {
        EntityValidationUtils.validStringLength("foodContent", foodContent, 0, 200);
        this.foodContent = foodContent;
    }

    public void setNote(String note) {
        EntityValidationUtils.validStringLength("note", note, 0, 200);
        this.note = note;
    }

    public void setDateTime(Instant dateTime) {
        EntityValidationUtils.validRecordDate("dateTime", dateTime);
        this.dateTime = dateTime;
    }
}
