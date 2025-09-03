package com.pena.app.service;

import com.pena.app.entity.AppUser;
import com.pena.app.entity.Member;
import com.pena.app.entity.VerificationToken;
import com.pena.app.repository.AppUserRepository;
import com.pena.app.repository.MemberRepository;
import com.pena.app.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerificationService {

    private final VerificationTokenRepository tokenRepository;
    private final AppUserRepository userRepository;
    private final MemberRepository memberRepository;
    private final EmailService emailService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public VerificationService(VerificationTokenRepository tokenRepository,
                               AppUserRepository userRepository,
                               MemberRepository memberRepository,
                               EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void sendVerificationEmail(AppUser user) {
        VerificationToken token = new VerificationToken(user);
        tokenRepository.save(token);
        String link = baseUrl + "/verify?token=" + token.getToken();
        String body = "<p>Bienvenido a la Peña Bética.</p>" +
                "<p>Por favor, verifica tu correo haciendo clic en el siguiente enlace:</p>" +
                "<p><a href=\"" + link + "\">Verificar correo</a></p>";
        String to = user.getUsername();
        emailService.send(to, "Verifica tu correo", body);
    }

    @Transactional
    public boolean verifyToken(String tokenValue) {
        VerificationToken token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));
        if (token.isUsed() || token.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            return false;
        }
        AppUser user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        Member m = user.getMember();
        if (m != null) {
            m.setEmailVerified(true);
            memberRepository.save(m);
        }
        token.setUsed(true);
        tokenRepository.save(token);
        return true;
    }
}
