package com.nsu.roomfinder.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage(Model model){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        System.out.println(name);
        System.out.println(authorities);

        return "login";
    }

}
