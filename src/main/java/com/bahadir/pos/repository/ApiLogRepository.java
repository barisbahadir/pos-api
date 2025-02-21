package com.bahadir.pos.repository;

import com.bahadir.pos.entity.log.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, String> {

    List<ApiLog> findByIdIsNotNullOrderByDateDesc();
}

