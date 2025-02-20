package com.bahadir.pos.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = -3555013890147827570L;

    private static final String SUCCESS_STATUS_MESSAGE = "OK";
    private static final int SUCCESS_STATUS_CODE = HttpStatus.OK.value();

    private int status;
    private String message;
    private T data;

    // Varsayılan başarılı yanıt (sadece status ve message ile)
    public ApiResponse() {
        this.status = SUCCESS_STATUS_CODE;
        this.message = SUCCESS_STATUS_MESSAGE;
    }

    // Başarılı yanıt oluşturma
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(SUCCESS_STATUS_CODE)
                .message(SUCCESS_STATUS_MESSAGE)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.FORBIDDEN.value())  // Hata durumları için 500 veya başka bir uygun kod
                .message(message)
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> error(int errorCode, String message) {
        return ApiResponse.<T>builder()
                .status(errorCode)  // Hata durumları için 500 veya başka bir uygun kod
                .message(message)
                .data(null)
                .build();
    }

}
