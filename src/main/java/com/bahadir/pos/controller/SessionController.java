package com.bahadir.pos.controller;

import com.bahadir.pos.entity.session.Session;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.security.SecuredEndpoint;
import com.bahadir.pos.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/list")
    public ResponseEntity<List<Session>> listAll() {
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/list/active")
    public ResponseEntity<List<Session>> listAllActiveSessions() {
        List<Session> sessions = sessionService.getActiveSessions();
        return ResponseEntity.ok(sessions);
    }

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/list/deactive")
    public ResponseEntity<List<Session>> listAllPassiveSessions() {
        List<Session> sessions = sessionService.getPassiveSessions();
        return ResponseEntity.ok(sessions);
    }

    // Tüm şirketleri sil
    @PostMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        sessionService.deleteAllPassiveSessions();
        return ResponseEntity.ok(true);
    }

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @PostMapping("/kill/all")
    public ResponseEntity<Integer> killAll() {
        int killedSessionCount = sessionService.killAllActiveSessions();
        return ResponseEntity.ok(killedSessionCount);
    }
}
