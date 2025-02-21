package com.bahadir.pos.service;

import com.bahadir.pos.entity.log.ApiLog;
import com.bahadir.pos.repository.ApiLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;
    private final SessionService sessionService;

    public ApiLogService(ApiLogRepository apiLogRepository,
                         SessionService sessionService) {
        this.apiLogRepository = apiLogRepository;
        this.sessionService = sessionService;
    }

    public List<ApiLog> getAllLogs() {
        return apiLogRepository.findByIdIsNotNullOrderByDateDesc();
    }

    public void saveApiLog(String token, String email, String requestUri,
                           String method, String requestBody, String responseBody,
                           int responseStatus) {

        String userSessionId = sessionService.findSessionIdByToken(token);

        ApiLog apiLog = new ApiLog();
        apiLog.setUserSessionId(userSessionId);
        apiLog.setEmail(email);
        apiLog.setRequestUri(requestUri);
        apiLog.setMethod(method);
        apiLog.setRequestBody(requestBody);
        apiLog.setResponseBody(responseBody);
        apiLog.setResponseStatus(responseStatus);
        apiLog.setDate(LocalDateTime.now());  // Zaman damgası ekliyoruz

        // Log'u veritabanına kaydediyoruz
        apiLogRepository.save(apiLog);
    }

    public void deleteAllLogs() {
        apiLogRepository.deleteAll();
    }
}
