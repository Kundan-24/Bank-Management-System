package com.bank.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bank.dto.UserDto;
import com.bank.helpers.Message;
import com.bank.helpers.MessageType;
import com.bank.services.UserService;

import jakarta.validation.Valid;

@Controller
public class PageController {
   
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String indexPage(){
        return "index";
    }

    @RequestMapping("/home")
    public String homePage(){       
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(){  
        return "about";
    }

    @RequestMapping("/contact")
    public String contactPage(){   
        return "contact";
    }

    @RequestMapping("/login")
    public String loginPage(){
        return "login";
    }

    @RequestMapping("/register")
    public String registerPage(Model model){
        if (!model.containsAttribute("userForm")) {
            model.addAttribute("userForm",new UserDto());
        }   
        return "register";
    }

    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute("userForm") UserDto userForm, BindingResult result, RedirectAttributes redirectAttributes){

        // validation errors
        if (result.hasErrors()) {
            return "register";
        }
        
        try {
            // service call
            UserDto savedUser = userService.registerUser(userForm);

            // seccess message
            Message message = Message.builder()
                                     .content("Registration Successful!")
                                     .type(MessageType.green)
                                     .build();

            redirectAttributes.addFlashAttribute("message", message);   
            return "redirect:/register";
        } catch (Exception ex) {
            // error message
            Message message = Message.builder()
                                     .content(ex.getMessage())
                                     .type(MessageType.red)
                                     .build();

                                     redirectAttributes.addFlashAttribute("message", message);
                                     return "redirect:/register";
        }
        
    }

}
