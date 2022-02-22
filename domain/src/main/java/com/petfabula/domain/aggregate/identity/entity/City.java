package com.petfabula.domain.aggregate.identity.entity;

import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.common.util.ValueUtil;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_city",
        indexes = {@Index(name = "prefecture_name_unique",  columnList="prefecture_id, name", unique = true)})
public class City extends EntityBase {

    public City(Long id, String name, String prefectureName, Long prefectureId) {
        setId(id);
        this.name = name;
        this.prefectureName = prefectureName;
        this.prefectureId = prefectureId;
    }

    @Column(name = "name", nullable = false, length = 16)
    private String name;

    @Column(name = "prefecture_name", nullable = false, length = 16)
    private String prefectureName;

    @Column(name = "prefecture_id", nullable = false)
    private Long prefectureId;

    public void setName(String name) {
        EntityValidationUtils.validStringLength("name", name, 1, 16);
        name = ValueUtil.trimContent(name);
        this.name = name;
    }
}
