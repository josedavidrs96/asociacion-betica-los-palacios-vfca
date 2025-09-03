package com.pena.app.controller;

import com.pena.app.entity.AppUser;
import com.pena.app.entity.MembershipFee;
import com.pena.app.entity.Member;
import com.pena.app.service.MembershipService;
import com.pena.app.service.PassService;
import com.pena.app.service.UserService;
import com.pena.app.repository.MemberRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/abonado")
public class AbonadoController {

    private final UserService userService;
    private final PassService passService;
    private final MembershipService membershipService;
    private final MemberRepository memberRepository;

    public AbonadoController(UserService userService, PassService passService, MembershipService membershipService, MemberRepository memberRepository) {
        this.userService = userService;
        this.passService = passService;
        this.membershipService = membershipService;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String dashboard(@AuthenticationPrincipal User principal, Model model) {
        AppUser u = userService.findByUsernameOrThrow(principal.getUsername());
        Member m = u.getMember();
        if (m != null) {
            MembershipFee current = membershipService.ensureCurrentMonthFee(m);
            model.addAttribute("member", m);
            model.addAttribute("passes", passService.listPassesForMember(m.getId()));
            model.addAttribute("fees", membershipService.listFeesForMember(m.getId()));
            model.addAttribute("currentFeeId", current.getId());
        }
        return "abonado_dashboard";
    }

    @PostMapping("/fees/{id}/pay")
    public String pay(@PathVariable Long id, @AuthenticationPrincipal User principal) {
        AppUser u = userService.findByUsernameOrThrow(principal.getUsername());
        if (u.getMember() != null) {
            membershipService.payFee(id, u.getMember().getId());
        }
        return "redirect:/abonado";
    }

    @PostMapping("/passes/new")
    public String newPass(@AuthenticationPrincipal User principal) {
        AppUser u = userService.findByUsernameOrThrow(principal.getUsername());
        if (u.getMember() != null) {
            passService.createPassForMember(u.getMember().getId());
        }
        return "redirect:/abonado";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User principal,
                                @RequestParam String phone,
                                @RequestParam String address) {
        AppUser u = userService.findByUsernameOrThrow(principal.getUsername());
        if (u.getMember() != null) {
            Member m = u.getMember();
            m.setPhone(phone);
            m.setAddress(address);
            memberRepository.save(m);
        }
        return "redirect:/abonado";
    }
}
