package com.bahadir.pos.controller;

import com.bahadir.pos.entity.log.ApiLog;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.security.SecuredEndpoint;
import com.bahadir.pos.service.ApiLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/log")
public class ApiLogController {

    private final ApiLogService apiLogService;

    public ApiLogController(ApiLogService apiLogService) {
        this.apiLogService = apiLogService;
    }

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/list")
    public ResponseEntity<List<ApiLog>> listAll() {
        List<ApiLog> allLogs = apiLogService.getAllLogs();
        return ResponseEntity.ok(allLogs);
    }

    // Tüm şirketleri sil
    @PostMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        apiLogService.deleteAllLogs();
        return ResponseEntity.ok(true);
    }
}
