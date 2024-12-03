package com.bahadir.pos.controller;

import com.bahadir.pos.entity.Product;
import com.bahadir.pos.entity.Transaction;
import com.bahadir.pos.entity.transaction.TransactionRequest;
import com.bahadir.pos.service.ProductService;
import com.bahadir.pos.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/add")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    // Bir transaction'ı id'ye göre al
    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }
}
