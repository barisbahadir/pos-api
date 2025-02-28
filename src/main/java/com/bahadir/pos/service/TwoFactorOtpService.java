package com.bahadir.pos.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import net.glxn.qrgen.QRCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class TwoFactorOtpService {

    @Value("${spring.application.name}")
    private String appName;

    private static final int CODE_DIGITS = 6; // OTP kod uzunluğu
    private static final int TIME_PERIOD = 30; // OTP süresi (saniye)

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public String getOtpAuthUri(String userEmail, String secretKey) {
        return String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s&digits=%d&period=%d",
                appName, userEmail, secretKey, appName, CODE_DIGITS, TIME_PERIOD
        );
    }

    public String generateQrCode(String email, String secretKey) {
        String otpAuthUri = getOtpAuthUri(email, secretKey);

        ByteArrayOutputStream stream = QRCode.from(otpAuthUri).withSize(250, 250).stream();
        return Base64.getEncoder().encodeToString(stream.toByteArray());
    }

    public boolean validateOtp(String secretKey, int userOtpCode) {
        return gAuth.authorize(secretKey, userOtpCode);
    }
}