package com.bahadir.pos.repository;

import com.bahadir.pos.entity.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    // Kullanıcının eski oturumlarını kapat
    @Modifying
    @Transactional
    @Query("UPDATE Session s SET s.logoutDate = :logoutDate WHERE s.email = :email AND s.logoutDate IS NULL")
    void expireOldSessionsWithEmail(String email, LocalDateTime logoutDate);

    @Modifying
    @Transactional
    @Query("UPDATE Session s SET s.lastAccessDate = :lastAccessDate WHERE s.token = :token")
    void updateByTokenLastAccessDate(@Param("token") String token, @Param("lastAccessDate") LocalDateTime lastAccessDate);

    // Token süresi dolmuş ama logout edilmemiş oturumları bulk update ile kapat
    @Modifying
    @Transactional
    @Query("UPDATE Session s SET s.logoutDate = :logoutTime WHERE s.logoutDate IS NULL AND s.tokenExpireDate < :now")
    int expireAllExpiredSessions(LocalDateTime now, LocalDateTime logoutTime);

    @Modifying
    @Transactional
    @Query("UPDATE Session s SET s.logoutDate = :logoutTime WHERE s.logoutDate IS NULL")
    int killAllActiveSessions(LocalDateTime logoutTime);

    // Girilen token icin oturum sonlandirilmis mi kontrolu
    boolean existsByTokenAndLogoutDateIsNotNull(String token);

    // Tüm sessionları createDate’e göre sıralı getir
    List<Session> findAllByOrderByLoginDateDesc();

    // Sadece aktif sessionları (finishDate NULL olanlar) getir ve createDate’e göre sırala
    List<Session> findByLogoutDateIsNullOrderByLoginDateDesc();

    // Sadece pasif sessionları (finishDate NULL olmayanlar) getir ve createDate’e göre sırala
    List<Session> findByLogoutDateIsNotNullOrderByLoginDateDesc();

    @Transactional
    void deleteByLogoutDateIsNotNull();

    Optional<Session> findByToken(String token);

    @Query("SELECT s.id FROM Session s WHERE s.token = :token")
    String findSessionIdByToken(String token);
}