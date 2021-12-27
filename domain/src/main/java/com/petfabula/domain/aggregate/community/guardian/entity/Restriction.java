package com.petfabula.domain.aggregate.community.guardian.entity;

import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.Instant;
import java.time.Period;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "participator_restriction")
public class Restriction extends EntityBase {

    public static final Instant PERMANENT_EXPIRATION = Instant.EPOCH;

    public Restriction(Long id, Long participatorId, String reason) {
        this(id, participatorId, PERMANENT_EXPIRATION, reason);
    }

    public Restriction(Long id, Long participatorId, Instant expiration, String reason) {
        setId(id);
        EntityValidationUtils.validStringLength("reason", reason, 0, 240);
        this.participatorId = participatorId;
        this.expiration = expiration;
        this.reason = reason;
    }

    @Column(name = "participator_id", unique = true, nullable = false)
    private Long participatorId;

    @Column(name = "expiration", nullable = false)
    private Instant expiration;

    @Column(name = "reason", length = 255)
    private String reason;

    public boolean expired(){
        return expiration.isBefore(Instant.now()) || !expiration.equals(PERMANENT_EXPIRATION);
    }

}
