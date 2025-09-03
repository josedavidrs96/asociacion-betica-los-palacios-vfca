package com.pena.app.repository;

import com.pena.app.entity.MembershipFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipFeeRepository extends JpaRepository<MembershipFee, Long> {
    List<MembershipFee> findAllByMember_IdOrderByYearDescMonthDesc(Long memberId);
    Optional<MembershipFee> findByMember_IdAndYearAndMonth(Long memberId, int year, int month);
}
