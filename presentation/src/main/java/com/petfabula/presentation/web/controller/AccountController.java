package com.petfabula.presentation.web.controller;

import com.petfabula.application.identity.IdentityApplicationService;
import com.petfabula.application.notification.NotificationApplicationService;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.identity.entity.City;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.CityRepository;
import com.petfabula.domain.aggregate.identity.repository.PrefectureRepository;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;
import com.petfabula.domain.aggregate.notification.respository.NotificationReceiverRepository;
import com.petfabula.domain.common.image.ImageFile;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.assembler.identity.CityAssembler;
import com.petfabula.presentation.facade.assembler.identity.UserAccountAssembler;
import com.petfabula.presentation.facade.dto.identity.CityDto;
import com.petfabula.presentation.facade.dto.identity.UserAccountDetailDto;
import com.petfabula.presentation.facade.dto.identity.UserAccountDto;
import com.petfabula.presentation.facade.dto.pet.PetDetailDto;
import com.petfabula.presentation.facade.dto.user.NotifySettingDto;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/account")
@Validated
public class AccountController {

    @Autowired
    private UserAccountAssembler userAccountAssembler;

    @Autowired
    private CityAssembler cityAssembler;

    @Autowired
    private IdentityApplicationService identityApplicationService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    @Autowired
    private NotificationApplicationService notificationApplicationService;

    @Autowired
    private CityRepository cityRepository;

    @GetMapping("account")
    public Response<UserAccountDetailDto> getAccountDetail() {
        Long userId = LoginUtils.currentUserId();
        UserAccount account = userAccountRepository.findById(userId);
        UserAccountDetailDto res = userAccountAssembler.convertToDetailDto(account);
        if (account.getCityId() != null) {
            City city = cityRepository.findById(account.getCityId());
            res.setCity(cityAssembler.convertToDto(city));
        }
        return Response.ok(res);
    }

    @PutMapping("account")
    public Response<UserAccountDetailDto> updateAccount(@RequestPart(name = "account") @Validated UserAccountDetailDto accountDto,
                                                        @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        Long userId = LoginUtils.currentUserId();
        ImageFile imageFile = null;
        if (image != null) {
            imageFile = new ImageFile(image.getOriginalFilename(),
                    image.getInputStream(), image.getSize());
        }
        UserAccount account = identityApplicationService
                .updateAccount(userId, AssemblerHelper.toLocalDate(accountDto.getBirthday()),
                        accountDto.getGender(), accountDto.getBio(), accountDto.getCityId(), imageFile);
        UserAccountDetailDto res = userAccountAssembler.convertToDetailDto(account);

        if (account.getCityId() != null) {
            City city = cityRepository.findById(account.getCityId());
            res.setCity(cityAssembler.convertToDto(city));
        }
        return Response.ok(res);
    }

    @GetMapping("cities")
    public Response<List<CityDto>> getCities() {
        List<City> cities = cityRepository.findAll();
        List<CityDto> cityDtos = cityAssembler.convertToDtos(cities);
        return Response.ok(cityDtos);
    }

    @GetMapping("notify-setting")
    public Response<NotifySettingDto> getNotifySetting() {
        Long userId = LoginUtils.currentUserId();
        NotificationReceiver receiver = notificationReceiverRepository.findById(userId);
        NotifySettingDto notifySettingDto = NotifySettingDto.builder()
                .receiveAnswerComment(receiver.isReceiveAnswerComment())
                .receiveFollow(receiver.isReceiveFollow())
                .receiveUpvote(receiver.isReceiveUpvote())
                .build();
        return Response.ok(notifySettingDto);
    }

    @PutMapping("notify-setting")
    public Response<NotifySettingDto> updateNotifySetting(@RequestBody NotifySettingDto notifySettingDto) {
        Long userId = LoginUtils.currentUserId();
        notificationApplicationService.udpateSetting(userId,
                notifySettingDto.isReceiveAnswerComment(),
                notifySettingDto.isReceiveFollow(),
                notifySettingDto.isReceiveUpvote());

        return Response.ok(notifySettingDto);
    }
}
