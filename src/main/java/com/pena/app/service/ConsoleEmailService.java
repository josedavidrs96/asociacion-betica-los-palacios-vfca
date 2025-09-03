package com.pena.app.service;

import org.springframework.stereotype.Service;

@Service
public class ConsoleEmailService implements EmailService {
    @Override
    public void send(String to, String subject, String htmlBody) {
        System.out.println("=== EMAIL SIMULADO ===");
        System.out.println("Para: " + to);
        System.out.println("Asunto: " + subject);
        System.out.println("Contenido: " + htmlBody);
        System.out.println("======================");
    }
}
