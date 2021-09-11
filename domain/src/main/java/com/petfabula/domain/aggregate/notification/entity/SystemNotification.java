package com.petfabula.domain.aggregate.notification.entity;

import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "notification_system")
public class SystemNotification extends GeneralEntity {

    public SystemNotification(Long id, Long senderId, String title, String content) {
        setId(id);
        this.senderId = senderId;
        setTitle(title);
        setContent(content);
    }

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    public void setTitle(String title) {
        EntityValidationUtils.validStringLength("title", content, 0, 50);
        this.title = title;
    }

    public void setContent(String content) {
        EntityValidationUtils.validStringLength("content", content, 0, 500);
        this.content = content;
    }
}
