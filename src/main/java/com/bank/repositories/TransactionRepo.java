package com.bank.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bank.entities.Account;
import com.bank.entities.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long>{

    List<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount);

    Page<Transaction> findAllByOrderByCreatedAtDesc(Pageable p);

    @Query("SELECT t FROM Transaction t WHERE DATE(t.createdAt) = CURRENT_DATE ORDER BY t.createdAt DESC")
    Page<Transaction> findTodayTransactions(Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :start AND :end ORDER BY t.createdAt DESC")
    Page<Transaction> findByDateRangeList(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.type = 'CREDIT' AND DATE(t.createdAt) = CURRENT_DATE")
    long countTodayCredit();

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.type = 'DEBIT' AND DATE(t.createdAt) = CURRENT_DATE")
    long countTodayDebit();

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.type = 'TRANSFER' AND DATE(t.createdAt) = CURRENT_DATE")
    long countTodayTransfers();

    long count();
}
