package com.bank.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.entities.Account;
import com.bank.entities.Transaction;
import com.bank.exceptions.ResourceNotFoundException;
import com.bank.helpers.TransactionType;
import com.bank.repositories.AccountRepo;
import com.bank.repositories.TransactionRepo;
import com.bank.services.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private TransactionRepo transactionRepo;
    

    @Transactional
    @Override
    public void debit(String fromAccount, String toAccount, BigDecimal amount) {
        
        Account sender = accountRepo.findByAccountNumber(fromAccount).orElseThrow(()-> new ResourceNotFoundException("Sender account number not found!!"));
        if (!sender.getUser().isEnabled()) {throw new RuntimeException("Your account is BLOCKED!");}
        if (!sender.isActive()) {throw new RuntimeException("Your account is FROZEN! please contact your bank");}

        Account receiver = accountRepo.findByAccountNumber(toAccount).orElseThrow(()-> new ResourceNotFoundException("Receiver account number not found!!"));
        if (!receiver.getUser().isEnabled()) {throw new RuntimeException("Receiver account is BLOCKED!");}

        if (fromAccount.equals(toAccount)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
             throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new ResourceNotFoundException("Insufficient balance!!");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepo.save(sender);
        accountRepo.save(receiver);

        Transaction txn = new Transaction();
        txn.setFromAccount(sender);
        txn.setToAccount(receiver);
        txn.setAmount(amount);
        txn.setType(TransactionType.DEBIT);

        transactionRepo.save(txn);
    }

    @Transactional
    @Override
    public void credit(String accountNumber, BigDecimal amount) {
       
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
             throw new IllegalArgumentException("Amount must be greater than zero");
        }

       Account account = accountRepo.findByAccountNumber(accountNumber).orElseThrow(()-> new ResourceNotFoundException("Account not found!!"));
       if (!account.getUser().isEnabled()) {throw new RuntimeException("Account is BLOCKED!");}
       account.setBalance(account.getBalance().add(amount));
       accountRepo.save(account);

       Transaction txn = new Transaction();
       txn.setFromAccount(null);
       txn.setToAccount(account);
       txn.setAmount(amount);
       txn.setType(TransactionType.CREDIT);

       transactionRepo.save(txn);
    }

    @Override
    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionRepo.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<Transaction> getTodayTransactions(Pageable pageable) {
       return transactionRepo.findTodayTransactions(pageable);
    }

    @Override
    public Page<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return transactionRepo.findByDateRangeList(start, end,pageable);
    }

    @Override
    public long todayCredit() {
        return transactionRepo.countTodayCredit();
    }

    @Override
    public long todayDebit() {
        return transactionRepo.countTodayDebit();
    }

    @Override
    public long todayTransfers() {
        return transactionRepo.countTodayTransfers();
    }

    @Override
    public long countTransactions() {
        return transactionRepo.count();
    }
}
