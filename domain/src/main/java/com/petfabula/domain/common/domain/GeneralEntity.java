package com.petfabula.domain.common.domain;

import lombok.Getter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@Getter
@MappedSuperclass
@FilterDef(name = "activeFilter", parameters = {@ParamDef(name = "delete_time", type = "Instant")})
@Filter(name = "activeFilter", condition = "delete_at = :delete_time")
public abstract class GeneralEntity extends EntityBase {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "delete_at")
    private Instant deleteAt = Instant.EPOCH;
//    private Instant deleteAt = null;


    public void markDelete() {
        this.deleteAt = Instant.now();
    }

    public boolean isDeleted() {
        return this.deleteAt != null;
    }
}
