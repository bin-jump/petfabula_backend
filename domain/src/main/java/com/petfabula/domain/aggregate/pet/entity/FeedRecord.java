package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.domain.GeneralEntity;
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
        indexes = {@Index(columnList = "pet_id, date")})
public class FeedRecord extends GeneralEntity {

    public FeedRecord(Long id, Long petId, Instant date, String foodContent, Integer amount, String note) {
        setId(id);
        this.petId = petId;
        this.date = date;
        this.foodContent = foodContent;
        this.amount = amount;
        this.note = note;
    }

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "food_content", nullable = false)
    private String foodContent;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "note")
    private String note;
}
