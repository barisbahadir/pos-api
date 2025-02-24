package com.bahadir.pos.service;

import com.bahadir.pos.entity.BaseStatus;
import com.bahadir.pos.entity.transaction.Transaction;
import com.bahadir.pos.entity.transaction.TransactionItem;
import com.bahadir.pos.entity.transaction.TransactionPaymentType;
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

    public List<Transaction> getTransactionsByProductNameOrBarcode(String keyword) {
        return transactionRepository.findByProductNameOrBarcode(keyword);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    public List<Transaction> getTransactions(String keyword, LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate != null && endDate != null && keyword != null && !keyword.isEmpty()) {
            return transactionRepository.findByKeywordAndDateRange(keyword, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            return transactionRepository.findByTransactionDateBetween(startDate, endDate);
        } else if (keyword != null && !keyword.isEmpty()) {
            return transactionRepository.findByProductNameOrBarcode(keyword);
        } else {
            return transactionRepository.findAll();
        }
    }


    public Transaction createTransaction(Transaction transaction) {
        Transaction finalTransaction = new Transaction();
        finalTransaction.setName(transaction.getName());
        finalTransaction.setDescription(transaction.getDescription());

        finalTransaction.setPaymentType(transaction.getPaymentType() != null ? transaction.getPaymentType() : TransactionPaymentType.CASH);
        finalTransaction.setTransactionItems(transaction.getTransactionItems());

        finalTransaction.setStatus(BaseStatus.ENABLE);
        finalTransaction.setTransactionDate(LocalDateTime.now()); // Satış tarihini şu anki zaman olarak ayarla

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
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    public void deleteAllTransactions() {
        transactionRepository.deleteAll();
    }
}
