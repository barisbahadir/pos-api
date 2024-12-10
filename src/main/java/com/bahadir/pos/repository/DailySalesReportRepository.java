package com.bahadir.pos.repository;

import com.bahadir.pos.entity.report.DailySalesReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySalesReportRepository extends JpaRepository<DailySalesReport, Long> {
}
