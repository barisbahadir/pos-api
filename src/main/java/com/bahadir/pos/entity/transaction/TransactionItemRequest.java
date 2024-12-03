package com.bahadir.pos.entity.transaction;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionItemRequest {
    private Long productId;  // Ürün ID'si
    private Integer quantity;  // Adet
    private BigDecimal price;  // Fiyat
}
