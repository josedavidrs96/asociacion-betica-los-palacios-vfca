package com.pena.app.controller;

import com.pena.app.entity.AppUser;
import com.pena.app.entity.Member;
import com.pena.app.service.MembershipService;
import com.pena.app.service.PassService;
import com.pena.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payment")
@PreAuthorize("hasRole('ABONADO')")
public class PaymentController {

    public enum ProductType { MEMBERSHIP_FEE, BUS_PASS_5 }
    public record CartItem(ProductType type, String name, double price) {}

    private final MembershipService membershipService;
    private final PassService passService;
    private final UserService userService;

    public PaymentController(MembershipService membershipService, PassService passService, UserService userService) {
        this.membershipService = membershipService;
        this.passService = passService;
        this.userService = userService;
    }

    @GetMapping
    public String paymentPage(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
        double total = cart == null ? 0.0 : cart.stream().mapToDouble(CartItem::price).sum();
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "payment";
    }

    @PostMapping("/charge")
    public String charge(@AuthenticationPrincipal User principal,
                         @RequestParam String cardNumber,
                         @RequestParam String expiry,
                         @RequestParam String cvc,
                         HttpSession session,
                         Model model) {
        // Simulación de pasarela: aceptar números que empiecen por 4242 y CVC de 3 dígitos
        if (cardNumber == null || !cardNumber.replaceAll("\\s+", "").startsWith("4242") || cvc.length() < 3) {
            model.addAttribute("error", "Pago rechazado. Verifica los datos de la tarjeta (usa 4242... para pruebas).");
            return paymentPage(session, model);
        }
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
        if (cart == null || cart.isEmpty()) {
            model.addAttribute("error", "Tu carrito está vacío.");
            return paymentPage(session, model);
        }
        AppUser u = userService.findByUsernameOrThrow(principal.getUsername());
        Member m = u.getMember();
        if (m == null) {
            model.addAttribute("error", "No se encontró el abonado asociado.");
            return paymentPage(session, model);
        }
        // Aplicar compra
        for (CartItem item : cart) {
            if (item.type() == ProductType.MEMBERSHIP_FEE) {
                var current = membershipService.ensureCurrentMonthFee(m);
                if (!current.isPaid()) {
                    membershipService.payFee(current.getId(), m.getId());
                }
            } else if (item.type() == ProductType.BUS_PASS_5) {
                passService.createPassForMember(m.getId());
            }
        }
        session.removeAttribute("CART");
        return "payment_success";
    }
}
