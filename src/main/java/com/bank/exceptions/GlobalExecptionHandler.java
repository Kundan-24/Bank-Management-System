package com.bank.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExecptionHandler {
   
    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExists(UserAlreadyExistsException ex, RedirectAttributes redirectAttributes){
        
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/signup";
    }

    @ExceptionHandler(Exception.class)
    public String handlerGenericExeption(Exception ex, RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong: "+ex.getMessage());
        return "redirect:/signup";
    }
}
