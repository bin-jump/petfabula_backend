package com.petfabula.presentation.web.controller;

import com.petfabula.application.identity.IdentityApplicationService;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.domain.aggregate.identity.repository.UserAccountRepository;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.assembler.identity.UserAccountAssembler;
import com.petfabula.presentation.facade.dto.identity.*;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.authentication.AuthenticationHelper;
import com.petfabula.presentation.web.authentication.AuthenticationProps;
import com.petfabula.presentation.web.security.LoginUtils;
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
@RequestMapping("/api/identity")
@Validated
public class IdentityController {

    @Autowired
    private UserAccountAssembler userAccountAssembler;

    @Autowired
    private IdentityApplicationService identityApplicationService;

    @Autowired
    private UserAccountRepository userAccountRepository;

//    @Autowired
//    private AuthenticationHelper authenticationHelper;

    @Autowired
    private AuthenticationProps authenticationProps;

    @GetMapping("me")
    public Response<UserAccountDto> loginUser() {
        Long userId = LoginUtils.currentUserId();
        if (userId == null) {
            return  Response.ok();
        }
        UserAccount user = userAccountRepository.findById(userId);
        UserAccountDto userAccountDto = userAccountAssembler.convertToDto(user);
        return Response.ok(userAccountDto);
    }

    @GetMapping("user-agreement")
    public Response<UserAgreementDto> userAgreement() {
        UserAgreementDto res = new UserAgreementDto("User Agreement", "User agreement content");
        return Response.ok(res);
    }

    // for local development
    @GetMapping("oauth-redirect")
    public void oauthRedirect(Device device,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        String q = request.getQueryString();
        q = q == null ? "" : "?" + q;
        String redirectPrefix = device.isMobile() ?
                authenticationProps.getMobileRedirect() : authenticationProps.getWebRedirect();
        log.info(String.format("redirect to %s%s", redirectPrefix, q));
        response.sendRedirect(String.format("%s%s", redirectPrefix, q));
    }

//    @PostMapping("register-signin-oauth")
//    public Response<UserAccountDto> registerAndLoginByOauth(@Validated @RequestBody OauthRequest request,
//                                                            HttpServletRequest req, HttpServletResponse response) {
//        UserAccount userAccount = identityApplicationService
//                .registerOrAuthenticateByOauth(request.getServerName(), request.getCode());
//        UserAccountDto userAccountDto = userAccountAssembler.convertToDto(userAccount);
//        authenticationHelper.signin(userAccount, req);
//
//        return Response.ok(userAccountDto);
//    }

//    @PostMapping("signin-email-code")
//    public Response<UserAccountDto> loginByEmailCode(@Validated @RequestBody EmailCodeLoginRequest request,
//                                                     HttpServletRequest req, HttpServletResponse response) {
//        UserAccount userAccount = identityApplicationService
//                .authenticateByEmailCode(request.getEmail(), request.getCode());
//        UserAccountDto userDto = userAccountAssembler.convertToDto(userAccount);
//        authenticationHelper.signin(userAccount, req);
//
//        return Response.ok(userDto);
//    }

//    @PostMapping("signin-email-password")
//    public Response<UserAccountDto> loginByEmailPassword(@Validated @RequestBody EmailPasswordLoginRequest request,
//                                                     HttpServletRequest req, HttpServletResponse response) {
//        UserAccount userAccount = identityApplicationService
//                .authenticateByEmailPassword(request.getEmail(), request.getPassword());
//        UserAccountDto userDto = userAccountAssembler.convertToDto(userAccount);
//        authenticationHelper.signin(userAccount, req);
//
//        return Response.ok(userDto);
//    }

//    @PostMapping("register-signin-email-code")
//    public Response<UserAccountDto> registerAndloginByEmailCode(@Validated @RequestBody EmailCodeRegisterRequest request,
//                                                                HttpServletRequest req, HttpServletResponse response) {
//        UserAccount userAccount = identityApplicationService
//                .registerByEmailCode(request.getName(), request.getEmail(), request.getCode());
//        UserAccountDto userAccountDto = userAccountAssembler.convertToDto(userAccount);
//        authenticationHelper.signin(userAccount, req);
//        return Response.ok(userAccountDto);
//    }

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
