package com.bahadir.pos.entity.log;

import com.bahadir.pos.utils.ApiUtils;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiLog {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")  // UUID'yi String olarak saklayacak şekilde
    private String id;

    @Column(columnDefinition = "TEXT")
    private String token;
    private String email;     // Kullanıcı adı (Varsa)

    private String httpMethod;   // GET, POST, PUT, DELETE
    private String endpoint;     // Hangi URL çağrılmış?
    private Integer statusCode;  // HTTP Response Kodu
    private String clientIp;     // Kullanıcının IP adresi
    private String responseStatus; // Yanıt durumu (SUCCESS / ERROR)

    private String errorType; // Hata Mesajı
    private String errorMessage; // Hata Mesajı

    @Column(columnDefinition = "TEXT")
    private String errorDetailedMessage; // Hata detaylarını içeren uzun mesaj

    @Column(nullable = false)
    private LocalDateTime logDate;

    @PrePersist
    public void prePersist() {
        if (this.errorDetailedMessage != null && this.errorDetailedMessage.length() > ApiUtils.MAX_TEXT_LENGTH) {
            this.errorDetailedMessage = this.errorDetailedMessage.substring(0, ApiUtils.MAX_TEXT_LENGTH); // Veriyi kes
        }
        this.logDate = LocalDateTime.now();
    }
}