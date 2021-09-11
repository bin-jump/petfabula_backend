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
@Table(name = "role")
public class Role extends EntityBase {

    public Role(Long id, String name) {
        setId(id);
        this.name = name;
    }

    @Column(name = "name", unique = true, nullable = false, length = 32)
    private String name;
}
