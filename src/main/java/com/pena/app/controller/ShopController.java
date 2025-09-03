package com.pena.app.controller;

import com.pena.app.entity.AppUser;
import com.pena.app.entity.Member;
import com.pena.app.service.MembershipService;
import com.pena.app.service.PassService;
import com.pena.app.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ShopController {

    public enum ProductType { MEMBERSHIP_FEE, BUS_PASS_5 }

    public record CartItem(ProductType type, String name, double price) {}

    private final PassService passService;
    private final MembershipService membershipService;
    private final UserService userService;

    public ShopController(PassService passService, MembershipService membershipService, UserService userService) {
        this.passService = passService;
        this.membershipService = membershipService;
        this.userService = userService;
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        model.addAttribute("products", List.of(
                new CartItem(ProductType.MEMBERSHIP_FEE, "Cuota mensual de abonado", 10.00),
                new CartItem(ProductType.BUS_PASS_5, "Pase de bus (5 viajes)", 5.00)
        ));
        return "shop";
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        List<CartItem> cart = getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("total", cart.stream().mapToDouble(CartItem::price).sum());
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam ProductType type, HttpSession session) {
        List<CartItem> cart = getCart(session);
        switch (type) {
            case MEMBERSHIP_FEE -> cart.add(new CartItem(type, "Cuota mensual de abonado", 10.00));
            case BUS_PASS_5 -> cart.add(new CartItem(type, "Pase de bus (5 viajes)", 5.00));
        }
        session.setAttribute("CART", cart);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("CART");
        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(@AuthenticationPrincipal User principal, HttpSession session) {
        List<CartItem> cart = getCart(session);
        if (cart.isEmpty()) return "redirect:/cart";
        AppUser u = userService.findByUsernameOrThrow(principal.getUsername());
        Member m = u.getMember();
        if (m == null) return "redirect:/cart";
        // Simular pago exitoso y aplicar efectos
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
        return "redirect:/abonado";
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        Object c = session.getAttribute("CART");
        if (c instanceof List<?> list) {
            return (List<CartItem>) list;
        }
        return new ArrayList<>();
    }
}
