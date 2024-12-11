package com.bahadir.pos.service;

import com.bahadir.pos.entity.report.DailySalesReport;
import com.bahadir.pos.entity.transaction.Transaction;
import com.bahadir.pos.entity.transaction.TransactionPaymentType;
import com.bahadir.pos.repository.DailySalesReportRepository;
import com.bahadir.pos.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SalesReportService {

    private final TransactionRepository transactionRepository;
    private final DailySalesReportRepository dailySalesReportRepository;

    public SalesReportService(TransactionRepository transactionRepository, DailySalesReportRepository dailySalesReportRepository) {
        this.transactionRepository = transactionRepository;
        this.dailySalesReportRepository = dailySalesReportRepository;
    }

    // Her gün gece 11:59'da çalışacak zamanlanmış görev
    @Scheduled(cron = "59 59 23 * * ?")
    public void generateDailySalesReport() {
        LocalDate today = LocalDate.now();

        // Bugün yapılmış tüm işlemleri al
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(
                today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        BigDecimal totalCardSales = BigDecimal.ZERO;
        BigDecimal totalCashSales = BigDecimal.ZERO;

        // Ödeme türlerine göre satışları ayır
        for (Transaction transaction : transactions) {
            if (transaction.getPaymentType() == TransactionPaymentType.CARD) {
                totalCardSales = totalCardSales.add(transaction.getTotalAmount());
            } else if (transaction.getPaymentType() == TransactionPaymentType.CASH) {
                totalCashSales = totalCashSales.add(transaction.getTotalAmount());
            }
        }

        BigDecimal totalAmount = totalCashSales.add(totalCardSales);

        // Raporu veritabanına ekle
        DailySalesReport report = new DailySalesReport(today, totalCardSales, totalCashSales, totalAmount);
        dailySalesReportRepository.save(report);

        // Eksik günleri kontrol et ve raporla
        checkAndGenerateMissingReports();
    }

    // Eksik günleri kontrol etme ve raporları oluşturma
    private void checkAndGenerateMissingReports() {
        // Veritabanındaki tüm günlük raporları al
        List<DailySalesReport> existingReports = dailySalesReportRepository.findAll();

        // Veritabanındaki ilk transaction tarihini bul
        Optional<Transaction> firstTransaction = transactionRepository.findFirstByOrderByTransactionDateAsc();
        if (!firstTransaction.isPresent()) {
            return; // Eğer hiç transaction yoksa, hiçbir rapor oluşturma
        }

        LocalDate firstTransactionDate = firstTransaction.get().getTransactionDate().toLocalDate();
        LocalDate endDate = LocalDate.now();

        // İlk kayıttan sonrasını kontrol et
        for (LocalDate date = firstTransactionDate; date.isBefore(endDate); date = date.plusDays(1)) {
            LocalDate finalDate = date;
            boolean isReportExist = existingReports.stream()
                    .anyMatch(report -> report.getReportDate().equals(finalDate));

            if (!isReportExist) {
                generateMissingReportForDay(date);
            }
        }
    }

    // Eksik gün için rapor oluşturma
    private void generateMissingReportForDay(LocalDate date) {
        // O gün yapılmış tüm işlemleri al
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(
                date.atStartOfDay(), date.plusDays(1).atStartOfDay());

        BigDecimal totalCardSales = BigDecimal.ZERO;
        BigDecimal totalCashSales = BigDecimal.ZERO;

        // Ödeme türlerine göre satışları ayır
        for (Transaction transaction : transactions) {
            if (transaction.getPaymentType() == TransactionPaymentType.CARD) {
                totalCardSales = totalCardSales.add(transaction.getTotalAmount());
            } else if (transaction.getPaymentType() == TransactionPaymentType.CASH) {
                totalCashSales = totalCashSales.add(transaction.getTotalAmount());
            }
        }
        BigDecimal totalAmount = totalCashSales.add(totalCardSales);

        // Raporu veritabanına ekle
        DailySalesReport report = new DailySalesReport(date, totalCardSales, totalCashSales, totalAmount);
        dailySalesReportRepository.save(report);
    }

    public void deleteAllReports() {
        dailySalesReportRepository.deleteAll();
    }
}
