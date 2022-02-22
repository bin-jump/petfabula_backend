package com.petfabula.domain.aggregate.administration.entity;

import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.util.ValueUtil;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_feedback")
public class Feedback extends GeneralEntity {

    public Feedback(Long id, Long reporterId, String content) {
        setId(id);
        this.reporterId = reporterId;
        setContent(content);
    }

    @Column(name = "reporter_id")
    private Long reporterId;

    @Column(name = "content", length = 1000)
    private String content;

    public void setContent(String content) {
        EntityValidationUtils.validStringLength("content", content, 10, 240);
        content = ValueUtil.trimContent(content);
        this.content = content;
    }
}
