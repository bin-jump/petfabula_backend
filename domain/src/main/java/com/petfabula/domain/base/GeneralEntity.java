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
@FilterDef(name = "activeFilter")
@Filter(name = "activeFilter", condition = "delete_at == null")
public abstract class GeneralEntity extends EntityBase {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "delete_at")
    private Instant deleteAt;

    public void markDelete() {
        this.deleteAt = Instant.now();
    }

    public boolean isDeleted() {
        return this.deleteAt != null;
    }
}
