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
    private BigDecimal cardSales;
    private BigDecimal cashSales;
    private BigDecimal totalAmount;

    public DailySalesReport(LocalDate reportDate, BigDecimal cardSales, BigDecimal cashSales, BigDecimal totalAmount) {
        this.reportDate = reportDate;
        this.cardSales = cardSales;
        this.cashSales = cashSales;
        this.totalAmount = totalAmount;
    }
}
