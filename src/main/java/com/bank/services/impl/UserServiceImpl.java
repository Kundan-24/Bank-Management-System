package com.bank.services.impl;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.bank.dto.UserDto;
import com.bank.entities.Account;
import com.bank.entities.Role;
import com.bank.entities.User;
import com.bank.exceptions.ResourceNotFoundException;
import com.bank.exceptions.UserAlreadyExistsException;
import com.bank.helpers.AccountNumberGenerator;
import com.bank.helpers.AccountType;
import com.bank.repositories.AccountRepo;
import com.bank.repositories.RoleRepo;
import com.bank.repositories.UserRepo;
import com.bank.services.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public UserDto registerUser(UserDto userDto) {
        
        // email check
        if (userRepo.findByEmail(userDto.getEmail()).isPresent()) {
             throw new UserAlreadyExistsException("Email alredy registered !!");
        }

        // aadhaar check
        if (userRepo.findByAadhaarNumber(userDto.getAadhaarNumber()).isPresent()) {
            throw new UserAlreadyExistsException("Aadhar already registered !!");
        }
       
        // role assign
        Role roleUser = roleRepo.findByName("ROLE_USER").orElseThrow(()-> new ResourceNotFoundException("Role not found"));
       
        // convert DTO to Entity
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAadhaarNumber(userDto.getAadhaarNumber());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setDob(userDto.getDob());
        user.setAddress(userDto.getAddress());
        user.setRoles(List.of(roleUser));
        
        // save
        User savedUser = userRepo.save(user);

        // create account
        if (userDto.isCreateAccount()) {
         String accountNumber = AccountNumberGenerator.generateAccountNumber();

            Account account = new Account();
            account.setAccountNumber(accountNumber);
            //account.setAccountType(userDto.getAccountType().toUpperCase());
            account.setAccountType(AccountType.valueOf(userDto.getAccountType().toUpperCase()));
            account.setBalance(BigDecimal.ZERO);
            // ye jo account create ho rha hai wo kis user ka hai 
            //  jo user upar saved ho raha us user ka hai
            // foreign key 
            account.setUser(savedUser);

            // object sync both sides
            savedUser.getAccounts().add(account);

            // saved account
            accountRepo.save(account);
        }

        

        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        // check old password
        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new ResourceNotFoundException("Old password is incorrect!!");
        }

        // set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }  
}
