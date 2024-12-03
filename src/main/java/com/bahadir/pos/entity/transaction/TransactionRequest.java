package com.bahadir.pos.entity.transaction;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransactionRequest {
    private List<TransactionItemRequest> items;  // Sepetteki ürünler
    private LocalDateTime transactionDate;  // İşlem tarihi
}
