package com.petfabula.domain.base;

import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.Instant;

@Getter
@MappedSuperclass
public abstract class ConcurrentEntity extends GeneralEntity {

    @Version
    @Column(name = "version")
    private Long version;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate = Instant.now();
}
