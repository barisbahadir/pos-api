package com.bahadir.pos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
public class TransactionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;  // Ürün adı
    private BigDecimal price;  // Ürün fiyatı
    private Integer quantity;  // Ürün miktarı

    // Constructor
    public TransactionItem(String productName, BigDecimal price, Integer quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
}
