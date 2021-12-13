package com.petfabula.infrastructure.notification.email;

import com.petfabula.domain.aggregate.identity.service.email.EmailSender;
import com.petfabula.domain.aggregate.identity.service.email.SendEmailRequest;
import com.petfabula.domain.exception.ExternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Slf4j
public class EmailSenderImpl implements EmailSender {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String senderName;

    @Override
    public void sendEmail(SendEmailRequest sendEmailRequest) {
        try {
//            SimpleMailMessage message = new SimpleMailMessage();
            //message.setFrom("");
//            message.setTo(sendEmailRequest.getAddress());
//            message.setSubject(sendEmailRequest.getTitle());
//            message.setText(sendEmailRequest.getContent());

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(sendEmailRequest.getContent(), true);
            helper.setTo(sendEmailRequest.getAddress());
            helper.setSubject(sendEmailRequest.getTitle());
            helper.setFrom(senderName);
            emailSender.send(mimeMessage);
        } catch (MailSendException | MessagingException ex) {
            log.error(ex.toString());
            throw new ExternalServerException(String.format("Failed to send email to %s", sendEmailRequest.getAddress()));
        }
    }
}
