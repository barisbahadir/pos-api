package com.bahadir.pos.controller;

import com.bahadir.pos.entity.Transaction;
import com.bahadir.pos.entity.TransactionFilterDto;
import com.bahadir.pos.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Ürünleri listele
    @GetMapping("/list")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAll();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestBody TransactionFilterDto filterDto) {
        List<Transaction> filteredTransactions =
                transactionService.getTransactions(filterDto.getSearchText(), filterDto.getStartDate(), filterDto.getEndDate());
        return ResponseEntity.ok(filteredTransactions);
    }

    @PostMapping("/add")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    // Bir transaction'ı id'ye göre al
    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAllTransactions() {
        transactionService.deleteAllTransactions();
        return ResponseEntity.ok(true);
    }
}
