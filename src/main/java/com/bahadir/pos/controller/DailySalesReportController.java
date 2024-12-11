package com.bahadir.pos.controller;

import com.bahadir.pos.entity.DataFilterDto;
import com.bahadir.pos.entity.report.DailySalesReport;
import com.bahadir.pos.repository.DailySalesReportRepository;
import com.bahadir.pos.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/filter")
    public List<DailySalesReport> filterAllDailyReports(@RequestBody DataFilterDto filterDto) {
        return dailySalesReportRepository.findByReportDateBetween(filterDto.getStartOnlyDate(), filterDto.getEndOnlyDate());
    }

    // Belirli bir tarihe ait satış raporunu döndüren endpoint
    @GetMapping("/list/{date}")
    public DailySalesReport getDailyReportByDate(@PathVariable LocalDate date) {
        return dailySalesReportRepository.findByReportDate(date)
                .orElseThrow(() -> new RuntimeException("Report not found for date: " + date));
    }

    // Günlük raporları manuel olarak tetikleyen endpoint
    @PostMapping("/generate-daily-report")
    public String generateDailySalesReport() {
        salesReportService.generateDailySalesReport();
        return "Daily Sales Report Generated Successfully";
    }

    //    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAllTransactions() {
        salesReportService.deleteAllReports();
        return ResponseEntity.ok(true);
    }
}
