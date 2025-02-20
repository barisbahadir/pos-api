package com.bahadir.pos.repository;

import com.bahadir.pos.entity.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    // Tüm sessionları createDate’e göre sıralı getir
    List<Session> findAllByOrderByLoginDateDesc();

    // Sadece aktif sessionları (finishDate NULL olanlar) getir ve createDate’e göre sırala
    List<Session> findByLogoutDateIsNullOrderByLoginDateDesc();

    // Sadece pasif sessionları (finishDate NULL olmayanlar) getir ve createDate’e göre sırala
    List<Session> findByLogoutDateIsNotNullOrderByLoginDateDesc();

    void deleteByLogoutDateIsNull();

    Optional<Session> findByToken(String token);

}