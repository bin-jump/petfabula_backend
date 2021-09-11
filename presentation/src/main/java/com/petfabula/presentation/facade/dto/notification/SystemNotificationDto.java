package com.petfabula.presentation.facade.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemNotificationDto {

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private Long createdDate;
}
