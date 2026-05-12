package com.bank.services;

import java.util.List;

import com.bank.dto.AccountDto;

public interface AccountService {

    List<AccountDto> getAllAccounts();

    List<AccountDto> searchAccounts(String keyword);

    void freezeAccount(Long accountId);

    void unfreezeAccount(Long accountId);

    long countAccounts();
    long countActiveAccounts();
    long countFrozenAccounts();
    double getTotalBalance();
}
