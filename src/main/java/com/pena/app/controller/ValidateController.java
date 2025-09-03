package com.pena.app.controller;

import com.pena.app.entity.BusPass;
import com.pena.app.service.PassService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/validate")
public class ValidateController {

    private final PassService passService;

    public ValidateController(PassService passService) {
        this.passService = passService;
    }

    @GetMapping
    public String form() {
        return "validate";
    }

    @PostMapping
    public String validate(@RequestParam("code") String code, Model model) {
        PassService.ValidationResult result = passService.validateCode(code.trim());
        model.addAttribute("code", code);
//        if (result.notFound().ok()) {
//            model.addAttribute("status", "notfound");
//            model.addAttribute("message", "Code not found");
//        } else
            if (result.noRemaining()) {
            BusPass p = result.pass();
            model.addAttribute("status", "noremain");
            model.addAttribute("message", "No remaining uses for this pass");
            model.addAttribute("remaining", p.getRemainingUses());
        } else {
            BusPass p = result.pass();
            model.addAttribute("status", "ok");
            model.addAttribute("message", "Validated successfully");
            model.addAttribute("remaining", p.getRemainingUses());
        }
        return "validate";
    }
}
