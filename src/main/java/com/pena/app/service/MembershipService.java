package com.pena.app.service;

import com.pena.app.entity.MembershipFee;
import com.pena.app.entity.Member;
import com.pena.app.repository.MembershipFeeRepository;
import com.pena.app.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MembershipService {

    private final MembershipFeeRepository feeRepository;
    private final MemberRepository memberRepository;

    public MembershipService(MembershipFeeRepository feeRepository, MemberRepository memberRepository) {
        this.feeRepository = feeRepository;
        this.memberRepository = memberRepository;
    }

    public List<MembershipFee> listFeesForMember(Long memberId) {
        return feeRepository.findAllByMember_IdOrderByYearDescMonthDesc(memberId);
    }

    @Transactional
    public MembershipFee ensureCurrentMonthFee(Member member) {
        LocalDate now = LocalDate.now();
        return feeRepository.findByMember_IdAndYearAndMonth(member.getId(), now.getYear(), now.getMonthValue())
                .orElseGet(() -> feeRepository.save(new MembershipFee(member, now.getYear(), now.getMonthValue())));
    }

    @Transactional
    public void payFee(Long feeId, Long memberId) {
        MembershipFee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new IllegalArgumentException("Cuota no encontrada"));
        if (!fee.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("No autorizado para pagar esta cuota");
        }
        fee.setPaid(true).setPaidAt(LocalDateTime.now());
        feeRepository.save(fee);
    }
}
