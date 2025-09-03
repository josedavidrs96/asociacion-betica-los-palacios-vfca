package com.pena.app.controller;

import com.google.zxing.WriterException;
import com.pena.app.entity.BusPass;
import com.pena.app.repository.MemberRepository;
import com.pena.app.service.PassService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/passes")
public class PassController {

    private final PassService passService;
    private final MemberRepository memberRepository;

    public PassController(PassService passService, MemberRepository memberRepository) {
        this.passService = passService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/new")
    public String newPassForm(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "passes_new";
    }

    @PostMapping("/new")
    public String createPass(@RequestParam("memberId") Long memberId) {
        BusPass pass = passService.createPassForMember(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return "redirect:/passes/" + pass.getId();
    }

    @GetMapping("/{id}")
    public String viewPass(@PathVariable Long id, Model model) {
        BusPass pass = passService.getPass(id).orElseThrow();
        model.addAttribute("pass", pass);
        String qrText = "PASS:" + pass.getId() + ":" + pass.getCode();
        model.addAttribute("qrUrl", "/passes/" + pass.getId() + "/qr.png");
        String msg = "Your%20bus%20pass%20code:%20" + pass.getCode() + "%20(uses%20left:%20" + pass.getRemainingUses() + ")";
        model.addAttribute("waLink", "https://wa.me/?text=" + msg);
        return "passes_view";
    }

    @GetMapping("/{id}/qr.png")
    @ResponseBody
    public ResponseEntity<byte[]> qr(@PathVariable Long id) throws WriterException, IOException {
        BusPass pass = passService.getPass(id).orElseThrow();
        String qrText = "PASS:" + pass.getId() + ":" + pass.getCode();
        byte[] png = passService.generateQrPng(qrText, 300);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"pass-" + id + ".png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(png);
    }
}
