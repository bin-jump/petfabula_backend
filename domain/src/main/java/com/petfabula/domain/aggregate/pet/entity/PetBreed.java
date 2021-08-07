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
@Table(name = "pet_breed")
public class PetBreed extends EntityBase {

    public PetBreed(Long id, String category, String name) {
        setId(id);
        this.category = category;
        this.name = name;
    }

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

}
