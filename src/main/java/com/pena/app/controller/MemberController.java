package com.pena.app.controller;

import com.pena.app.entity.Member;
import com.pena.app.repository.MemberRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String newMemberForm(Model model) {
        model.addAttribute("member", new Member());
        return "members_new";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String createMember(@ModelAttribute Member member) {
        memberRepository.save(member);
        return "redirect:/";
    }
}
