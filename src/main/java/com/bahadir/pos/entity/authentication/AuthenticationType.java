package com.bahadir.pos.entity.authentication;

public enum AuthenticationType {
    NONE,     // 2FA devre dışı
    OTP,     // Google Authenticator (TOTP)
    EMAIL,     // E-posta ile doğrulama kodu
}
