package com.bahadir.pos.service;

import com.bahadir.pos.entity.Product;
import com.bahadir.pos.entity.Transaction;
import com.bahadir.pos.entity.TransactionItem;
import com.bahadir.pos.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Transaction createTransaction(Transaction transaction) {
        Transaction finalTransaction = new Transaction();
        finalTransaction.setTransactionDate(LocalDateTime.now()); // Satış tarihini şu anki zaman olarak ayarla
        finalTransaction.setTransactionItems(transaction.getTransactionItems()); // Sepetteki ürünleri transaction'a ekle

        BigDecimal totalAmount = calculateTotalAmount(transaction.getTransactionItems()); // Sepet toplamı hesapla
        finalTransaction.setTotalAmount(totalAmount); // Hesaplanan tutarı transaction'a ekle

        return transactionRepository.save(finalTransaction);
    }

    private BigDecimal calculateTotalAmount(List<TransactionItem> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (TransactionItem item : items) {
            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        return totalAmount;
    }

    // Transaction'ı id'ye göre bul
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    // Tüm ürünleri sil
    public void deleteAllTransactions() {
        transactionRepository.deleteAll();
    }
}
