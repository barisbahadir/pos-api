package com.bahadir.pos.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = -3555013890147827570L;

    private static final String SUCCESS_STATUS_MESSAGE = "OK";
    private static final int SUCCESS_STATUS_CODE = HttpStatus.OK.value();

    @Builder.Default
    private int status = SUCCESS_STATUS_CODE;

    @Builder.Default
    private String message = SUCCESS_STATUS_MESSAGE;

    private String errorSource;
    private String detailedMessage;

    private T data;

    private boolean isSuccess;

    @Builder.Default
    private String date = DateTimeUtils.formatLocalDateTime(LocalDateTime.now());

    // Varsayılan başarılı yanıt (sadece status ve message ile)
    public ApiResponse() {
        this.isSuccess = true;
        this.status = SUCCESS_STATUS_CODE;
        this.message = SUCCESS_STATUS_MESSAGE;
        this.date = DateTimeUtils.formatLocalDateTime(LocalDateTime.now());
    }

    // Başarılı yanıt oluşturma
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .message(SUCCESS_STATUS_MESSAGE)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int errorCode, Throwable exception, String errorSource) {
        return ApiResponse.<T>builder()
                .isSuccess(false)
                .status(errorCode)
                .message(exception.getMessage())
                .detailedMessage(ApiUtils.getStackTraceMessage(exception))
                .errorSource(errorSource)
                .data(null)
                .build();
    }
}
