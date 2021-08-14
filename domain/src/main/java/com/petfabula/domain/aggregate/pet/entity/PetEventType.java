package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "pet_event_type")
public class PetEventType extends EntityBase {

    public PetEventType(Long id, String category, String eventType) {
        setId(id);
        this.category = category;
        this.eventType = eventType;
    }

    @Column(name = "category")
    private String category;

    @Column(name = "event_type", nullable = false, unique = true)
    private String eventType;
}
