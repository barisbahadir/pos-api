package com.bahadir.pos.service;

import com.bahadir.pos.entity.session.Session;
import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.repository.SessionRepository;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createSession(User user, HttpServletRequest request, String token) {
        Session session = new Session();
        session.setUser(user);
        session.setLoginTime(LocalDateTime.now());
        session.setUserRole(user.getAuthRole().name());
        session.setIpAddress(request.getRemoteAddr());
        session.setUserAgent(request.getHeader("User-Agent"));
        session.setToken(token);
        session.setLastAccessTime(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    public void closeSessionByToken(String token) {
        Session session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Session not found for token"));
        session.setLogoutTime(LocalDateTime.now());
        sessionRepository.save(session);
    }

    public void validateAndUpdateSession(String token) {
        Session session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Session not found for token"));
        session.setLastAccessTime(LocalDateTime.now());
        sessionRepository.save(session);
    }

    public Session findSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public void deleteSession(Long sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}