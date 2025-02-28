package com.bahadir.pos.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorMailService {

    @Value("${spring.application.name}")
    private String appName;

    private final JavaMailSender mailSender;

    public TwoFactorMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public int sendOtpEmail(String toEmail, int otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(appName + " - 2FA OTP Kodu");
            helper.setText("Merhaba,\n\nGiriş için OTP kodunuz: " + otpCode + "\n\nKodun süresi 5 dakikadır.", false);

            mailSender.send(message);

            return otpCode;
        } catch (MessagingException e) {
            throw new RuntimeException("E-posta gönderimi başarısız oldu!", e);
        }
    }
}