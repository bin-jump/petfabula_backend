package com.petfabula.domain.aggregate.community.entity;

import com.petfabula.domain.base.ConcurrentEntity;
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
@Table(name = "participator_pet",
        indexes = {@Index(name = "name_index",  columnList="name", unique = true)})
public class ParticipatorPet extends ConcurrentEntity {

    public ParticipatorPet(Long id, Long participatorId, String name, String photo) {
        setId(id);
        this.participatorId = participatorId;
        setName(name);
        setPhoto(photo);
    }

    @Column(name = "participator_id", nullable = false)
    private Long participatorId;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "photo", nullable = false)
    private String photo;

    public void setName(String name) {
        EntityValidationUtils.validPetName("name", name);
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
