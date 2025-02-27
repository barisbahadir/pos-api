package com.bahadir.pos.repository;

import com.bahadir.pos.entity.log.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, String> {

    List<ApiLog> findByIdIsNotNullOrderByLogDateDesc();

    //Girilen tarihten gunumuze kadar olan kayitlari yeniden eskiye siralanmis sekilde getirir
    @Query("SELECT a FROM ApiLog a WHERE a.logDate >= :startDate ORDER BY a.logDate DESC")
    List<ApiLog> findLogsFromStartDate(@Param("startDate") LocalDateTime startDate);

}

