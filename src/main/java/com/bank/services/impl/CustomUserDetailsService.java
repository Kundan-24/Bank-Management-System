package com.bank.services.impl;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bank.entities.User;
import com.bank.repositories.UserRepo;

import lombok.var;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
         User user = userRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        
         // convert roles to GrantedAuthorities
         var authorities = user.getRoles()
                               .stream()
                               .map(role -> new SimpleGrantedAuthority(role.getName()))
                               .collect(Collectors.toList());

         return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            authorities
         );
    }

    

}
