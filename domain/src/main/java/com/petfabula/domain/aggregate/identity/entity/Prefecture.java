package com.petfabula.domain.aggregate.identity.entity;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_prefecture")
public class Prefecture extends EntityBase {

    public Prefecture(Long id, String name) {
        setId(id);
        this.name = name;
    }

    @Column(name = "name", nullable = false, unique = true, length = 16)
    private String name;
}
