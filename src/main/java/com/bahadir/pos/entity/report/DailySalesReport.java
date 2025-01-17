package com.bahadir.pos.entity.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.bahadir.pos.utils.DateTimeUtils.DEFAULT_DATE_FORMAT;

@Data
@Getter
@Entity
@NoArgsConstructor
public class DailySalesReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
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
