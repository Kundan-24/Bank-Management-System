package com.bank.controllers;

import com.bank.repositories.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.bank.entities.User;

@ControllerAdvice
public class RootController {
    @Autowired
    private  UserRepo userRepo;

    
    @ModelAttribute("currentUser")
    public User currentUser(Authentication authentication){
         if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
             return null;
         }
        return userRepo.findByEmail(authentication.getName()).orElse(null);
    }
}
