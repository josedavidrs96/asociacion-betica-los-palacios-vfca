package com.pena.app.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pena.app.entity.BusPass;
import com.pena.app.entity.RideUse;
import com.pena.app.repository.BusPassRepository;
import com.pena.app.repository.MemberRepository;
import com.pena.app.repository.RideUseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class PassService {

    private final MemberRepository memberRepository;
    private final BusPassRepository busPassRepository;
    private final RideUseRepository rideUseRepository;

    @PersistenceContext
    private EntityManager em;

    public PassService(MemberRepository memberRepository,
                       BusPassRepository busPassRepository,
                       RideUseRepository rideUseRepository) {
        this.memberRepository = memberRepository;
        this.busPassRepository = busPassRepository;
        this.rideUseRepository = rideUseRepository;
        ensureDataFolder();
    }

    private void ensureDataFolder() {
        try {
            Path data = Path.of("data");
            if (!Files.exists(data)) {
                Files.createDirectories(data);
            }
        } catch (IOException ignored) {
        }
    }

    @Transactional
    public Optional<BusPass> createPassForMember(Long memberId) {
        return memberRepository.findById(memberId).map(member -> {
            String code = generateCode();
            BusPass pass = new BusPass(member, code);
            pass.setTotalUses(5);
            pass.setUsedCount(0);
            pass.setActive(true);
            return busPassRepository.save(pass);
        });
    }

    public Optional<BusPass> getPass(Long id) {
        return busPassRepository.findById(id);
    }

    public Optional<BusPass> findByCode(String code) {
        return busPassRepository.findByCode(code);
    }

    public List<BusPass> listPassesForMember(Long memberId) {
        return em.createQuery("select p from BusPass p where p.member.id = :memberId order by p.createdAt desc", BusPass.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<BusPass> listAllPasses() {
        return em.createQuery("select p from BusPass p order by p.createdAt desc", BusPass.class)
                .getResultList();
    }

    @Transactional
    public void setActive(Long passId, boolean active) {
        BusPass pass = busPassRepository.findById(passId)
                .orElseThrow(() -> new IllegalArgumentException("Pase no encontrado"));
        pass.setActive(active);
        busPassRepository.save(pass);
    }

    @Transactional
    public ValidationResult validateCode(String code) {
        Optional<BusPass> opt = busPassRepository.findByCode(code);
        if (opt.isEmpty()) {
//            return ValidationResult.notFound();
        }
        BusPass pass = opt.get();
        if (!pass.canUse()) {
            return ValidationResult.noRemaining(pass);
        }
        pass.consumeOne();
        busPassRepository.save(pass);
        rideUseRepository.save(new RideUse(pass));
        return ValidationResult.ok(pass);
    }

    public byte[] generateQrPng(String text, int size) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", os);
            return os.toByteArray();
        }
    }

    private String generateCode() {
        String base = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String code = base.substring(0, 8);
        int suffix = new Random().nextInt(900) + 100;
        return code + "-" + suffix;
    }

    /**
     * Record que representa el resultado de validación de un código de pase de autobús.
     *
     * @param ok true si la validación fue exitosa
     * @param notFound true si el código no fue encontrado
     * @param noRemaining true si el pase existe pero no tiene usos restantes
     * @param pass el pase asociado (null si notFound es true)
     */
    public record ValidationResult(boolean ok, boolean notFound, boolean noRemaining, BusPass pass) {

        public static ValidationResult ok(BusPass pass) {
            return new ValidationResult(true, false, false, pass);
        }

//        public static ValidationResult notFound() {
//            return new ValidationResult(false, true, false, null);
//        }

        public static ValidationResult noRemaining(BusPass pass) {
            return new ValidationResult(false, false, true, pass);
        }
    }
}
