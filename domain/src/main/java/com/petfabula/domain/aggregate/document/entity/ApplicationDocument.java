package com.petfabula.domain.aggregate.document.entity;

import com.petfabula.domain.common.domain.ConcurrentEntity;
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
@Table(name = "application_document")
public class ApplicationDocument extends ConcurrentEntity {

    public ApplicationDocument(Long id, String documentKey, String content) {
        setId(id);
        this.documentKey = documentKey;
        setContent(content);
    }

    @Column(name = "document_key", nullable = false, length = 64, unique = true)
    private String documentKey;

    @Column(name = "content", nullable = false, length = 65000, columnDefinition = "TEXT")
    private String content;

    public void setContent(String content) {
        EntityValidationUtils.validStringLength("content", content, 0, 60000);
        this.content = content;
    }

}
