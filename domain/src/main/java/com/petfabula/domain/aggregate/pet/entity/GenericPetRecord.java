package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "pet_generic_record", uniqueConstraints={
        @UniqueConstraint(columnNames = {"record_id", "record_type"})})
public class GenericPetRecord extends EntityBase {

    public GenericPetRecord(Long id, Long petId, Long recordId, String recordType) {
        setId(id);
        this.petId = petId;
        this.recordId = recordId;
        this.recordType = recordType;
    }

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Column(name = "record_type", nullable = false)
    private String recordType;

}
