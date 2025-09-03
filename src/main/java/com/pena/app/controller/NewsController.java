package com.pena.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewsController {

    @GetMapping("/news")
    public String news(Model model) {
        model.addAttribute("items", java.util.List.of(
                new NewsItem("Victoria en el derbi", "El Betis consigue una gran victoria en el derbi.", "https://upload.wikimedia.org/wikipedia/en/6/6f/Real_betis_logo.svg"),
                new NewsItem("Nuevo fichaje", "Presentación del nuevo jugador verdiblanco.", "https://upload.wikimedia.org/wikipedia/en/6/6f/Real_betis_logo.svg")
        ));
        return "news";
    }

    public record NewsItem(String title, String summary, String imageUrl) {}
}
