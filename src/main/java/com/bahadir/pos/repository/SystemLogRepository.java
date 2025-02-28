package com.bahadir.pos.repository;

import com.bahadir.pos.entity.log.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, String> {

    List<SystemLog> findByIdIsNotNullOrderByLogDateDesc();

    //Girilen tarihten gunumuze kadar olan kayitlari yeniden eskiye siralanmis sekilde getirir
    @Query("SELECT a FROM SystemLog a WHERE a.logDate >= :startDate ORDER BY a.logDate DESC")
    List<SystemLog> findLogsFromStartDate(@Param("startDate") LocalDateTime startDate);

}

