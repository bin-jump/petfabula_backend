package com.petfabula.domain.aggregate.identity.service;

import com.petfabula.domain.aggregate.identity.MessageKey;
import com.petfabula.domain.aggregate.identity.repository.VerificationCodeRepository;
import com.petfabula.domain.aggregate.identity.service.email.EmailSender;
import com.petfabula.domain.aggregate.identity.service.email.SendEmailRequest;
import com.petfabula.domain.exception.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VerificationCodeService {

    public static final int REGISTER_CODE_VALID_LIMIT_MINUTE = 20;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;


//    public static String emailPasswordReigsterkey(String email) {
//        return "petfabula.register.email.key::" + email;
//    }
//
//    public static String emailForgotPasswordkey(String email) {
//        return "petfabula.forgotpassed.email.key::" + email;
//    }

    public static String emailCodeReigsterkey(String email) {
        return "petfabula.register.email.key::" + email;
    }

    public static String emailCodeLoginkey(String email) {
        return "petfabula.login.email.key::" + email;
    }


//    public void generateAndNotifyWithEmailPasswordRegister(String email) {
//        String code = randomNumericToken();
//        SendEmailRequest sendEmailRequest = verificationEmailRequest(email, code);
//        verificationCodeRepository
//                .save(emailPasswordReigsterkey(email), code, REGISTER_CODE_VALID_LIMIT_MINUTE * 60);
//        emailSender.sendEmail(sendEmailRequest);
//    }

    public void generateAndNotifyWithEmailCodeRegister(String email) {
        String code = randomNumericToken();
        SendEmailRequest sendEmailRequest = verificationEmailRequest(email, code);
        verificationCodeRepository
                .save(emailCodeReigsterkey(email), code, REGISTER_CODE_VALID_LIMIT_MINUTE * 60);
        emailSender.sendEmail(sendEmailRequest);
    }

    public void generateAndNotifyWithEmailCodeLogin(String email) {
        String code = randomNumericToken();
        SendEmailRequest sendEmailRequest = verificationEmailRequest(email, code);
        verificationCodeRepository
                .save(emailCodeLoginkey(email), code, REGISTER_CODE_VALID_LIMIT_MINUTE * 60);
        emailSender.sendEmail(sendEmailRequest);
    }

    public void checkEmailLoginCode(String email, String code) {
        String saved = verificationCodeRepository.getCode(emailCodeLoginkey(email));
        if (StringUtils.compare(code, saved) != 0) {
            throw new InvalidValueException("code", MessageKey.INVALID_VERIFICATION_CODE);
        }
    }

    public void checkEmailCodeRegisterCode(String email, String code) {
        String saved = verificationCodeRepository.getCode(emailCodeReigsterkey(email));
        if (StringUtils.compare(code, saved) != 0) {
            throw new InvalidValueException("code", MessageKey.INVALID_VERIFICATION_CODE);
        }

    }

    public void removeEmailLoginCode(String email) {
        verificationCodeRepository.remove(emailCodeLoginkey(email));
    }

    public void removeEmailCodeRegisterCode(String email) {
        verificationCodeRepository.remove(emailCodeReigsterkey(email));
    }


    private SendEmailRequest verificationEmailRequest(String email, String code) {
        SendEmailRequest sendEmailRequest = new SendEmailRequest();

        String content = "ペットファビュラをご利用いただきありがとうございます。\n" +
                "以下の認証コードで、手続きを完了してください。\n" +
                "\n   " + code + " \n\n" +
                "なお、この認証コードの有効期間は" + REGISTER_CODE_VALID_LIMIT_MINUTE + "分以内となります。\n" +
                "\n ＊このメールに関して覚えがない方は本メールを無視してください。\n" +
                "このメールへご返信は受け付けておりません。\n";

        sendEmailRequest.setAddress(email);
        sendEmailRequest.setTitle("【ペットファビュラ】アカウント認証コード");
        sendEmailRequest.setContent(content);
        return sendEmailRequest;
    }

    private String randomNumericToken() {
        return RandomStringUtils.randomNumeric(6);
    }
}
