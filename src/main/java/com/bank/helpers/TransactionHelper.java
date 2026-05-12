package com.bank.helpers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bank.entities.Account;
import com.bank.entities.Transaction;
import com.bank.repositories.TransactionRepo;

@Component
public class TransactionHelper {

    @Autowired
    private TransactionRepo transactionRepo;

    public List<Transaction> getTransactions(Account account) {
        return transactionRepo.findByFromAccountOrToAccount(account, account);
    }

    public boolean isIncoming(Transaction txn, Account account) {
        return txn.getToAccount() != null &&
               txn.getToAccount().getAccountNumber().equals(account.getAccountNumber());
    }

    public boolean isOutgoing(Transaction txn, Account account) {
        return txn.getFromAccount() != null &&
               txn.getFromAccount().getAccountNumber().equals(account.getAccountNumber());
    }
}