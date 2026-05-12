package com.bank.helpers;

import org.springframework.stereotype.Component;

import com.bank.entities.Account;
import com.bank.entities.User;

@Component
public class AccountHelper {
    public  Account getPrimaryAccount(User user){
        return (user.getAccounts() != null && !user.getAccounts().isEmpty()) ? user.getAccounts().get(0) : null;
    }
}
