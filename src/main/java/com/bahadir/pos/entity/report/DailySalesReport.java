package com.bahadir.pos.entity.report;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
public class DailySalesReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reportDate;
    private BigDecimal totalCardSales;
    private BigDecimal totalCashSales;
    private BigDecimal totalAmount;

    public DailySalesReport(LocalDate reportDate, BigDecimal totalCardSales, BigDecimal totalCashSales, BigDecimal totalAmount) {
        this.reportDate = reportDate;
        this.totalCardSales = totalCardSales;
        this.totalCashSales = totalCashSales;
        this.totalAmount = totalAmount;
    }
}
