package com.bahadir.pos.service;

import com.bahadir.pos.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SessionCleanupService {

    @Value("${session.cleanup.interval}")
    private long cleanupInterval;

    private final SessionRepository sessionRepository;

    public SessionCleanupService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    // 5 dakikada bir çalışır
    @Scheduled(fixedRateString = "${session.cleanup.interval}")
    @Transactional
    public void cleanExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();

        int expiredSessionCount = sessionRepository.expireAllExpiredSessions(now, now);
        if (expiredSessionCount > 0)
            System.out.println("JWT Expire suresi dolduğu için kapatılan oturum sayisi: " + expiredSessionCount);

//        List<Session> activeSessions = sessionRepository.findByLogoutDateIsNullOrderByLoginDateDesc();
//
//        for (Session session : activeSessions) {
//            if (session.getTokenExpireDate().isBefore(now)) {
//                session.setLogoutDate(now);
//                sessionRepository.save(session);
//                System.out.println("Oturum süresi dolduğu için kapatıldı: " + session.getEmail() + " - " + session.getId() );
//            }
//        }
    }
}
