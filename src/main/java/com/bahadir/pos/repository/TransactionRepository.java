package com.bahadir.pos.repository;

import com.bahadir.pos.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN t.transactionItems ti " +
            "WHERE ti.productName LIKE %:keyword% OR ti.barcode LIKE %:keyword%")
    List<Transaction> findByProductNameOrBarcode(@Param("keyword") String keyword);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByTransactionDateBetween(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t JOIN t.transactionItems ti " +
            "WHERE (ti.productName LIKE %:keyword% OR ti.barcode LIKE %:keyword%) " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByKeywordAndDateRange(@Param("keyword") String keyword,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

}
