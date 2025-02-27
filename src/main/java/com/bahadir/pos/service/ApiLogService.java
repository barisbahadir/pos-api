package com.bahadir.pos.service;

import com.bahadir.pos.entity.log.ApiLog;
import com.bahadir.pos.repository.ApiLogRepository;
import com.bahadir.pos.utils.ApiUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;

    public ApiLogService(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    public List<ApiLog> getAllLogs() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return apiLogRepository.findLogsFromStartDate(yesterday);
    }

    public ApiLog saveLog(HttpServletRequest request, Throwable exception, HttpStatus httpStatus, String errorSource) {
        String message = "";
        try {
            message = ApiUtils.getExceptionMessage(exception);
            ApiLog log = ApiLog.builder()
                    .email(request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "Anonymous")
                    .token(ApiUtils.getJwtFromRequest(request))
                    .httpMethod(request.getMethod())
                    .endpoint(request.getRequestURI())
                    .clientIp(request.getRemoteAddr())
                    .statusCode(httpStatus.value())
                    .errorType(errorSource)
                    .errorMessage(message)
                    .errorDetailedMessage(ApiUtils.getStackTraceMessage(exception))
                    .logDate(LocalDateTime.now())
                    .build();

            return apiLogRepository.save(log);
        } catch (Exception exc) {
            System.out.println("Api log kaydedilirken bir hata olustu. Exc: " + message);
        }
        return null;
    }

    public void deleteAllLogs() {
        apiLogRepository.deleteAll();
    }
}
