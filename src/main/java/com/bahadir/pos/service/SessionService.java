package com.bahadir.pos.service;

import com.bahadir.pos.entity.authentication.AuthenticationType;
import com.bahadir.pos.entity.session.Session;
import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.repository.SessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    @Value("${single-device-session}") // Burada bayrağı alıyoruz
    private boolean isSingleDeviceSession;

    private final SessionRepository sessionRepository;

    private final LocalDateTime defaultFromDate = LocalDateTime.now().minusDays(2);

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findSessionsFromStartDate(defaultFromDate);
    }

    public List<Session> getActiveSessions() {
        return sessionRepository.findActiveSessionsFromStartDate(defaultFromDate);
    }

    public List<Session> getPassiveSessions() {
        return sessionRepository.findDeactiveSessionsFromStartDate(defaultFromDate);
    }

    @Transactional
    public Session createSession(User user, LocalDateTime tokenExpireDate, HttpServletRequest request, String token) {

        if (isSingleDeviceSession) {
            expireOldSessions(user.getEmail());
        }

        Session session = new Session();
        session.setToken(token);
        session.setUsername(user.getUsername());
        session.setEmail(user.getEmail());
        session.setUserRole(user.getAuthRole().name());
        session.setLoginType(user.getAuthType() == AuthenticationType.NONE ? "PASSWORD" : user.getAuthType().name());
        session.setLoginDate(LocalDateTime.now());
        session.setLastAccessDate(LocalDateTime.now());
        session.setTokenExpireDate(tokenExpireDate);
        session.setIpAddress(request.getRemoteAddr());
        session.setUserAgent(request.getHeader("User-Agent"));
        return sessionRepository.save(session);
    }

    @Transactional
    public void closeSessionByToken(String token) {
        Session session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Session not found for token"));
        LocalDateTime now = LocalDateTime.now();
        session.setLogoutDate(now);
        session.setLastAccessDate(now);
        sessionRepository.save(session);
    }

    public void updateSessionLastAccessDate(String token) {
        sessionRepository.updateByTokenLastAccessDate(token, LocalDateTime.now());
    }

    public void expireOldSessions(String email) {
        sessionRepository.expireOldSessionsWithEmail(email, LocalDateTime.now());
    }

    public boolean isSessionInvalid(String token) {
        return sessionRepository.isSessionInvalid(token);
    }

    public Session findSessionById(String sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public String findSessionIdByToken(String token) {
        try {
            return sessionRepository.findSessionIdByToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    @Transactional
    public void deleteAllPassiveSessions() {
        sessionRepository.deleteByLogoutDateIsNotNull();
    }

    @Transactional
    public int killAllActiveSessions() {
        LocalDateTime now = LocalDateTime.now();
        return sessionRepository.killAllActiveSessions(now);
    }
}