package com.bank.controllers;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bank.dto.AccountDto;
import com.bank.dto.UserDto;
import com.bank.entities.Transaction;
import com.bank.helpers.Message;
import com.bank.helpers.MessageType;
import com.bank.services.AccountService;
import com.bank.services.AdminService;
import com.bank.services.TransactionService;
import com.bank.services.UserService;

@Controller
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    
    @RequestMapping("/dashboard")
    public String adminHandler(Model model){
        // Top Stats
        model.addAttribute("totalUsers", adminService.countUsers());
        model.addAttribute("totalAccounts", accountService.countAccounts());
        model.addAttribute("totalTransactions", transactionService.countTransactions());
        model.addAttribute("totalBalance", accountService.getTotalBalance());

        // Transaction Summary (Today)
        model.addAttribute("todayCredit", transactionService.todayCredit());
        model.addAttribute("todayDebit", transactionService.todayDebit());
        model.addAttribute("todayTransfers", transactionService.todayTransfers());

        // Account Summary
        model.addAttribute("activeAccounts", accountService.countActiveAccounts());
        model.addAttribute("frozenAccounts", accountService.countFrozenAccounts());
        model.addAttribute("blockedUsers", adminService.countBlockedUsers());
        return "admin/adminPage";
    }
 
    // Admin Add Money 
    @PostMapping("/credit")
    public String addMoney(@RequestParam String accountNumber, @RequestParam BigDecimal amount, RedirectAttributes redirectAttributes){
        try {
            transactionService.credit(accountNumber, amount);
            redirectAttributes.addFlashAttribute("message",
            Message.builder()
                   .content("Amount added successfully")
                   .type(MessageType.green)
                   .build()
        );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
            Message.builder()
                   .content(e.getMessage())
                   .type(MessageType.red)
                   .build()
        );
        }
        return "redirect:/admins/dashboard";
    }

// **********usermanagement********************
    @GetMapping("/users")
    public String getAllUsers(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "9") int size,
         Model model){

         if ("null".equals(keyword)) {
             keyword = null;
            }   

         Page<UserDto> usersPage;   
        
        if(keyword != null && !keyword.trim().isEmpty()){
            usersPage = adminService.searchUsers(keyword, PageRequest.of(page, size));
        } else{
            usersPage = adminService.getAllUsers(PageRequest.of(page, size));
        }

        model.addAttribute("usersPage", usersPage);
        model.addAttribute("keyword", keyword == null ? "" : keyword);

        return "admin/usermanagement";
    }

    // DELETE
    @GetMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId){
        adminService.deleteUser(userId);
        return "redirect:/admins/users";
    }

    // BLOCK
    @GetMapping("/users/block/{userId}")
    public String blockUser(@PathVariable Long userId){
        adminService.blockUser(userId);
        return "redirect:/admins/users";
    }

    // UNBLOCK
    @GetMapping("/users/unblock/{userId}")
    public String unblockUser(@PathVariable Long userId){
        adminService.unblockUser(userId);
        return "redirect:/admins/users";
    }

// **********Account monitoring**************************
    @GetMapping("/accounts")
    public String getAccounts(@RequestParam(required = false) String keyword, Model model){
        
        List<AccountDto> accounts;

        if (keyword != null && !keyword.isEmpty()) {
            accounts = accountService.searchAccounts(keyword);
        }else{
            accounts = accountService.getAllAccounts();
        }

        long totalAccounts = accounts.size();
        long activeAccounts = accounts.stream().filter(AccountDto::isActive).count();
        long frozenAccounts = accounts.stream().filter(a-> !a.isActive()).count();

       double totalBalance  = accounts.stream().mapToDouble(AccountDto::getBalance).sum();


        model.addAttribute("accounts", accounts);
        model.addAttribute("totalAccounts", totalAccounts);
        model.addAttribute("activeAccounts", activeAccounts);
        model.addAttribute("frozenAccounts", frozenAccounts);
        model.addAttribute("totalBalance", totalBalance);
        return"admin/account-management";
    }

    // FREEZE ACCOUNT
    @GetMapping("/accounts/freeze/{accountId}")
    public String freezeAccount(@PathVariable Long accountId){
        accountService.freezeAccount(accountId);
        return "redirect:/admins/accounts";
    }

    // UNFREEZE ACCOUNT
    @GetMapping("/accounts/unfreeze/{accountId}")
    public String unfreezeAccount(@PathVariable Long accountId){
        accountService.unfreezeAccount(accountId);
        return "redirect:/admins/accounts";
    }

// **********TRANSACTION MONITORING****************
     @GetMapping("/transactions")
     public String getAllTransactions(
        @RequestParam(required = false) String filter,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size,
        Model model)
        {   
         
            if(filter == null ) filter = "";
            if(startDate == null) startDate = "";
            if (endDate == null) endDate = ""; 

           Page<Transaction> transactions;
           
           if("today".equals(filter)){
            transactions = transactionService.getTodayTransactions(PageRequest.of(page, size));
           }else if (!startDate.isEmpty() && !endDate.isEmpty()) {
             LocalDate start = LocalDate.parse(startDate);
             LocalDate end = LocalDate.parse(endDate);

            transactions = transactionService.getTransactionsByDateRange(start.atStartOfDay(), end.atTime(23, 59, 59), PageRequest.of(page, size));
           }else if(!startDate.isEmpty()){
             LocalDate date = LocalDate.parse(startDate);

             transactions = transactionService.getTransactionsByDateRange(date.atStartOfDay(), date.atTime(23, 59, 59), PageRequest.of(page, size));
           } else{
            transactions = transactionService.getAllTransactions(PageRequest.of(page, size));
           }
            
        model.addAttribute("transactionsPage", transactions);
        model.addAttribute("filter", filter);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("currentPage", page);

        return "admin/transaction-management";
     } 


}
