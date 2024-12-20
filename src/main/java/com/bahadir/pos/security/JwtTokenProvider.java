package com.bahadir.pos.security;

import com.bahadir.pos.exception.JwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final String PRIVATE_KEY_PATH = "keys/private.key";  // Private key dosyası
    private final String PUBLIC_KEY_PATH = "keys/public.key";    // Public key dosyası

    private static final long JWT_EXPIRATION = 86400000; // 1 gün

    // JWT token üretme (RS256 imza algoritması kullanarak)
    public String generateJwtToken(String username, List<String> roles) throws JwtTokenException {
        try {
            PrivateKey privateKey = getPrivateKeyFromFile(PRIVATE_KEY_PATH);

            return Jwts.builder()
                    .setSubject(username)
                    .claim("roles", roles) // Rolleri burada ekliyoruz
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                    .signWith(privateKey, SignatureAlgorithm.RS256)  // RS256 kullanarak imzalama
                    .compact();
        } catch (Exception e) {
            throw new JwtTokenException("Error generating JWT token", e);
        }
    }

    // JWT token doğrulama
    public boolean validateJwtToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token geçersizse, hata mesajı ve 403 dönebiliriz
            throw new JwtTokenException("Invalid JWT token", e); // Özel hata fırlatma
        } catch (Exception e) {
            throw new JwtTokenException("Error validating JWT token", e); // Genel hata durumunda özel mesaj
        }
    }

    // JWT token'dan claim bilgilerini almak
    public Claims getClaimsFromToken(String token) throws JwtTokenException {
        try {
            PublicKey publicKey = getPublicKeyFromFile(PUBLIC_KEY_PATH);
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)  // Public key ile doğrulama
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new JwtTokenException("Error parsing JWT token", e);
        }
    }

    private PrivateKey getPrivateKeyFromFile(String filePath) throws JwtTokenException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new JwtTokenException("Private key file not found in classpath");
            }

            byte[] keyBytes = inputStream.readAllBytes();

            // Anahtar formatını kontrol et
            String key = new String(keyBytes);
            key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decodedKey = Base64.getDecoder().decode(key);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            return keyFactory.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new JwtTokenException("Error loading private key from file", e);
        }
    }

    private PublicKey getPublicKeyFromFile(String filePath) throws JwtTokenException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new JwtTokenException("Public key file not found in classpath");
            }

            byte[] keyBytes = inputStream.readAllBytes();

            // Anahtar formatını kontrol et
            String key = new String(keyBytes);
            key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decodedKey = Base64.getDecoder().decode(key);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            return keyFactory.generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new JwtTokenException("Error loading public key from file", e);
        }
    }
}
