package com.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bank.services.impl.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

      @Autowired
      private MyAuthSuccessHandler successHandler;
      
      @Autowired
      private CustomUserDetailsService userDetailsService;

      @Bean
      public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
       return authProvider;
      }

      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
            .authorizeHttpRequests(auth ->{ auth
              
              // ROLE ACCESS
              .requestMatchers("/","/login","/register","/do-register","/home","/css/**","/js/**","/images/**").permitAll()
              .requestMatchers("/admins/**").hasRole("ADMIN")
              .requestMatchers("/users/**").hasRole("USER")
              
              // AND ANY ROUTES PUBLIC
              .anyRequest().permitAll();
      });

            // LOGIN
            http.csrf(AbstractHttpConfigurer::disable);
            http.formLogin(formLogin ->{
              formLogin.loginPage("/login");
              formLogin.loginProcessingUrl("/authenticate");
              formLogin.successHandler(successHandler);
              formLogin.usernameParameter("email");
              formLogin.passwordParameter("password");
            });
            
            // LOGOUT 
            http.logout(logoutForm ->{
              logoutForm.logoutUrl("/logout");
              logoutForm.logoutSuccessUrl("/login?logout=true");
            });

            return http.build();         
      }
      

      @Bean
      public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
      }

     
}
