package com.petfabula.domain.common.idgenerator;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@Data
@Entity
@Table(name = "id_generate_segment")
public class IdSegment {

    public IdSegment(String serviceTag, Integer step, Long initialMaxId) {
        this.serviceTag = serviceTag;
        this.step = step;
        this.maxId = initialMaxId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @Column(name = "service_tag", nullable = false, unique = true)
    private String serviceTag;

    @Column(name = "max_id", nullable = false)
    private Long maxId;

    @Column(name = "step", nullable = false)
    private Integer step;

    @Column(name = "description")
    private String desc;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedTime = Instant.now();
}
