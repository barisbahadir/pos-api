package com.bahadir.pos.controller;

import com.bahadir.pos.entity.DataFilterDto;
import com.bahadir.pos.entity.transaction.Transaction;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.security.SecuredEndpoint;
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

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/list")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAll();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestBody DataFilterDto filterDto) {
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

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAllTransactions() {
        transactionService.deleteAllTransactions();
        return ResponseEntity.ok(true);
    }
}
