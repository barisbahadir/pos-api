package com.bahadir.pos.controller;

import com.bahadir.pos.entity.report.DailySalesReport;
import com.bahadir.pos.service.SalesReportService;
import com.bahadir.pos.repository.DailySalesReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class DailySalesReportController {

    @Autowired
    private DailySalesReportRepository dailySalesReportRepository;

    @Autowired
    private SalesReportService salesReportService;

    // Günlük satış raporlarını tüm listeyi döndüren endpoint
    @GetMapping("/list")
    public List<DailySalesReport> getAllDailyReports() {
        return dailySalesReportRepository.findAll();
    }

    // Belirli bir tarihe ait satış raporunu döndüren endpoint
    @GetMapping("/list/{date}")
    public DailySalesReport getDailyReportByDate(@PathVariable LocalDate date) {
        return dailySalesReportRepository.findByReportDate(date)
                .orElseThrow(() -> new RuntimeException("Report not found for date: " + date));
    }

    // Raporları belirli bir tarih aralığına göre döndüren endpoint
    @GetMapping("/list/range")
    public List<DailySalesReport> getReportsInRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return dailySalesReportRepository.findByReportDateBetween(startDate, endDate);
    }

    // Günlük raporları manuel olarak tetikleyen endpoint
    @PostMapping("/generate-daily-report")
    public String generateDailySalesReport() {
        salesReportService.generateDailySalesReport();
        return "Daily Sales Report Generated Successfully";
    }
}
