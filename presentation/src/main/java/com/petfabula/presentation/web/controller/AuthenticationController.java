package com.petfabula.presentation.web.controller;

import com.petfabula.application.identity.IdentityApplicationService;
import com.petfabula.presentation.facade.dto.identity.EmailCodeNotifyCodeRequest;
import com.petfabula.presentation.facade.dto.identity.ExamineEmailRegisterAndSendCodeRequest;
import com.petfabula.presentation.web.api.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthenticationController {

    @Autowired
    private IdentityApplicationService identityApplicationService;

    @PostMapping("register-send-code")
    public Response emailCodeRegisterVerificationCode(@Validated @RequestBody ExamineEmailRegisterAndSendCodeRequest request) {
        identityApplicationService.examineEmailCodeRegisterContentAndSendCode(request.getName(), request.getEmail());
        return Response.ok(request.getName());
    }

    @PostMapping("signin-email-send-code")
    public Response loginByEmailSendCode(@Validated @RequestBody EmailCodeNotifyCodeRequest request) {
        identityApplicationService.sendEmailCodeLoginCode(request.getEmail());
        return Response.ok(request.getEmail());
    }
}
