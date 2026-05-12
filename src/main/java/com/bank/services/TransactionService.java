package com.bank.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bank.entities.Transaction;

public interface TransactionService {
     
    void debit(String fromAccount, String toAccount, BigDecimal amount);

    void credit(String accountNumber, BigDecimal amount);

    Page<Transaction> getAllTransactions(Pageable pageable);

    Page<Transaction> getTodayTransactions(Pageable pageable);

    Page<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable);

    long todayCredit();
    long todayDebit();
    long todayTransfers();
    long countTransactions();


}
