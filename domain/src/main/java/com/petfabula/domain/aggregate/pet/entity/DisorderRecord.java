package com.petfabula.domain.aggregate.pet.entity;

import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import com.petfabula.domain.exception.InvalidOperationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(name = "disorderRecord.all",
        attributeNodes = {@NamedAttributeNode("images")}
)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "pet_disorder_record",
        indexes = {@Index(columnList = "pet_id, date_time, delete_at", unique = true)})
public class DisorderRecord extends GeneralEntity {

    public DisorderRecord(Long id, Long petId, Instant dateTime, String disorderType, String content) {
        setId(id);
        this.petId = petId;
        setDateTime(dateTime);
        this.disorderType = disorderType;
        setContent(content);
    }

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "date_time", nullable = false)
    private Instant dateTime;

    @Column(name = "disorder_type")
    private String disorderType;

    @Column(name = "content", length = 3000)
    private String content;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="disorder_record_id", foreignKey = @ForeignKey(name = "none"))
    private List<DisorderRecordImage> images = new ArrayList<>();

    public void addImage(DisorderRecordImage image) {
        if (images.size() == 6) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        images.add(image);
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public void setDisorderType(String disorderType) {
        this.disorderType = disorderType;
    }

    public void setContent(String content) {
        EntityValidationUtils.validStringLength("content", content, 0, 500);
        this.content = content;
    }

    public void setDateTime(Instant dateTime) {
        EntityValidationUtils.validRecordDate("dateTime", dateTime);
        this.dateTime = dateTime;
    }

    public void removeImage(Long id) {
        DisorderRecordImage image = images.stream().
                filter(p -> p.getId().equals(id)).
                findFirst().orElse(null);
        if (image != null) {
            images.removeIf(x -> x.getId().equals(id));
        }
    }
}
