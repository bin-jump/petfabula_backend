package com.petfabula.domain.base;

import lombok.Getter;

import javax.persistence.*;

@Getter
@MappedSuperclass
public class AutoIdEntity implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;
}
