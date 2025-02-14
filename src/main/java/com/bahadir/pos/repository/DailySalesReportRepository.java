package com.bahadir.pos.repository;

import com.bahadir.pos.entity.report.DailySalesReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailySalesReportRepository extends JpaRepository<DailySalesReport, Long> {

    // Belirli bir tarihe ait raporu al
    Optional<DailySalesReport> findByReportDate(LocalDate date);

    // Belirli bir tarih aralığında raporları al
    List<DailySalesReport> findByReportDateBetween(LocalDate startDate, LocalDate endDate);

}
