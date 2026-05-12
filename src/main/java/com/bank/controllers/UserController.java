package com.bank.controllers;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bank.entities.Transaction;
import com.bank.exceptions.ResourceNotFoundException;
import com.bank.helpers.AccountHelper;
import com.bank.helpers.Message;
import com.bank.helpers.MessageType;
import com.bank.helpers.TransactionHelper;
import com.bank.repositories.TransactionRepo;
import com.bank.repositories.UserRepo;
import com.bank.services.TransactionService;
import com.bank.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private AccountHelper accountHelper;

    @Autowired
    private TransactionHelper transactionHelper;
   
    
    @RequestMapping("/dashboard")
    public String userDashboard(@AuthenticationPrincipal User userDetails  , Model model){
        System.out.println("userDashboard");
        com.bank.entities.User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        var account = accountHelper.getPrimaryAccount(user);

        model.addAttribute("user", user);
        model.addAttribute("account", accountHelper.getPrimaryAccount(user));

        var transactions = transactionRepo.findByFromAccountOrToAccount(account, account);
        transactions.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        // only latest 3
        List<Transaction> recentTransaction = transactions.stream().limit(3).toList();
        model.addAttribute("transactions", recentTransaction);
        return "user/userPage";
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.GET)   
    public String transferPage(@AuthenticationPrincipal User userDetails, Model model){
      com.bank.entities.User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow(()-> new ResourceNotFoundException("User not found!!"));
      model.addAttribute("user", user);
      model.addAttribute("account", accountHelper.getPrimaryAccount(user));
        return "user/transfer";
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String transferMoney(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam BigDecimal amount, RedirectAttributes redirectAttributes){
        try {
            transactionService.debit(fromAccount, toAccount, amount);
            Message message = Message.builder()
                                 .content("Transfer Successfully")
                                 .type(MessageType.green)
                                 .build();
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            Message message = Message.builder()
                                    .content(e.getMessage())
                                    .type(MessageType.red)
                                    .build();
            redirectAttributes.addFlashAttribute("message", message);
        }
        return "redirect:/users/transfer";
    }


    @RequestMapping("/balance")
    public String balancePage(@AuthenticationPrincipal User userDetails, Model model){
         com.bank.entities.User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
         model.addAttribute("user", user);
         model.addAttribute("account", accountHelper.getPrimaryAccount(user));
        return "user/balance";
    }

    @RequestMapping("/transactions")
    public String transactionsPage(@AuthenticationPrincipal User userDetails, Model model){
        com.bank.entities.User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow(()-> new ResourceNotFoundException("User not found!!"));
    
        var account = accountHelper.getPrimaryAccount(user);
       
        model.addAttribute("user", user);
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactionHelper.getTransactions(account));
        
        return "user/transactions";
    }

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String PpasswordPage(){
       return "user/password";
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, Principal principal){
        userService.changePassword(principal.getName(), oldPassword, newPassword);
        return "redirect:/logout?success=true";
    }

    
}
