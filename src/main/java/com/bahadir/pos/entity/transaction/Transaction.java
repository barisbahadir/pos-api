package com.bahadir.pos.entity.transaction;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime transactionDate;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private TransactionPaymentType paymentType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "transaction_id")
    private List<TransactionItem> transactionItems;
    
    private BigDecimal calculateTotalAmount(List<TransactionItem> items) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (TransactionItem item : items) {
            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);  // Toplam tutarı ekle
        }

        return totalPrice;  // Hesaplanan toplam tutarı döndür
    }

    public Transaction(LocalDateTime transactionDate, List<TransactionItem> transactionItems) {
        this.transactionDate = transactionDate;
        this.transactionItems = transactionItems;
        this.totalAmount = calculateTotalAmount(transactionItems);
    }
}
