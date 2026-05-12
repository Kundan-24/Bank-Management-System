package com.bank.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.dto.AccountDto;
import com.bank.entities.Account;
import com.bank.exceptions.ResourceNotFoundException;
import com.bank.repositories.AccountRepo;
import com.bank.services.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ModelMapper mapper;
    
    @Override
    public List<AccountDto> getAllAccounts() {
        return accountRepo.findAll().stream().map(account -> mapper.map(account, AccountDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> searchAccounts(String keyword) {
       return accountRepo.searchAccounts(keyword).stream().map(account -> mapper.map(account, AccountDto.class)).collect(Collectors.toList());      
    }

    @Override
    public void freezeAccount(Long accountId) {
        Account account = accountRepo.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("Account not found!!"));
        account.setActive(false);
        accountRepo.save(account);
    }

    @Override
    public void unfreezeAccount(Long accountId) {
        Account account = accountRepo.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("Account not found!!"));
        account.setActive(true);
        accountRepo.save(account);
    }

    @Override
    public long countAccounts() {
        return accountRepo.count();
    }

    @Override
    public long countActiveAccounts() {
        return accountRepo.countByActiveTrue();
    }

    @Override
    public long countFrozenAccounts() {
       return accountRepo.countByActiveFalse();
    }

    @Override
    public double getTotalBalance() {
        return accountRepo.getTotalBalance();
    }

}
