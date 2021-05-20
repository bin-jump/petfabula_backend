package com.petfabula.domain.base;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@Getter
@MappedSuperclass
@FilterDef(name = "activeFilter", parameters = {@ParamDef(name = "activeMark", type = "java.time.Instant")})
@Filter(name = "activeFilter", condition = "delete_time = :activeMark")
public abstract class GeneralEntity extends EntityBase {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "delete_time", nullable = false)
    private Instant deleteTime = Instant.EPOCH;

    public void markDelete() {
        this.deleteTime = Instant.now();
    }

    public boolean isDeleted() {
        return this.deleteTime == Instant.EPOCH;
    }
}
