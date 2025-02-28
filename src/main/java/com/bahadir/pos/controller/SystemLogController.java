package com.bahadir.pos.controller;

import com.bahadir.pos.entity.log.SystemLog;
import com.bahadir.pos.service.SystemLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/log")
public class SystemLogController {

    private final SystemLogService systemLogService;

    public SystemLogController(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<SystemLog>> listAll() {
        List<SystemLog> allLogs = systemLogService.getAllLogs();
        return ResponseEntity.ok(allLogs);
    }

    // Tümünü sil
    @PostMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        systemLogService.deleteAllLogs();
        return ResponseEntity.ok(true);
    }
}
