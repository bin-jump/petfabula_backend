package com.petfabula.presentation.facade.assembler.notification;

import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import com.petfabula.presentation.facade.dto.notification.SystemNotificationDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SystemNotificationAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public SystemNotificationDto convertToDto(SystemNotification notification) {
        SystemNotificationDto notificationDto = modelMapper
                .map(notification, SystemNotificationDto.class);
        return notificationDto;
    }

    public List<SystemNotificationDto> convertToDtos(List<SystemNotification> notifications) {
        return notifications.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
