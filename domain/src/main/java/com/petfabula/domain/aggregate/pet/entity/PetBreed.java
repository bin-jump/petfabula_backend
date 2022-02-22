package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.common.util.ValueUtil;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "pet_breed",
        indexes = {@Index(name = "category_index",  columnList="category_id, name", unique = true)})
public class PetBreed extends EntityBase {

    public PetBreed(Long id, Long categoryId, String category, String name) {
        setId(id);
        this.categoryId = categoryId;
        this.category = category;
        this.name = name;
    }

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "name", nullable = false)
    private String name;

    public void setName(String title) {
        EntityValidationUtils.validStringLength("name", title, 1, 20);
        name = ValueUtil.trimContent(name);
        this.name = title;
    }
}
