package com.pena.app.controller;

import com.pena.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String phone,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String address,
                           @RequestParam String email,
                           Model model) {
        try {
            if (userService.registerAbonado(name, phone, email, address, username, password).isEnabled()) {
                model.addAttribute("ok", true);
                return "login";
            } else {
                model.addAttribute("error", "Registration failed");
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
