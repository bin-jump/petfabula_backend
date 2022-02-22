package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.common.util.ValueUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "pet_category")
public class PetCategory extends EntityBase {

    // DOG, CAT, RABBIT, BIRD, HAMSTER, OTHER, PLANT,

    public PetCategory(Long id, String name) {
        setId(id);
        setName(name);
    }

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    public void setName(String name) {
        name = ValueUtil.trimContent(name);
        this.name = name;
    }
}
