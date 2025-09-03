package com.pena.app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class MembershipFee {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;

    private int year;
    private int month; // 1-12

    @Column(nullable = false)
    private BigDecimal amount = new BigDecimal("10.00");

    private boolean paid = false;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    public MembershipFee() {}

    public MembershipFee(Member member, int year, int month) {
        this.member = member;
        this.year = year;
        this.month = month;
    }

    public Long getId() { return id; }

    public Member getMember() { return member; }

    public MembershipFee setMember(Member member) { this.member = member; return this; }

    public int getYear() { return year; }

    public MembershipFee setYear(int year) { this.year = year; return this; }

    public int getMonth() { return month; }

    public MembershipFee setMonth(int month) { this.month = month; return this; }

    public BigDecimal getAmount() { return amount; }

    public MembershipFee setAmount(BigDecimal amount) { this.amount = amount; return this; }

    public boolean isPaid() { return paid; }

    public MembershipFee setPaid(boolean paid) { this.paid = paid; return this; }

    public LocalDateTime getPaidAt() { return paidAt; }

    public MembershipFee setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; return this; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public MembershipFee setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

    @Transient
    public boolean isForCurrentMonth() {
        java.time.LocalDate now = java.time.LocalDate.now();
        return this.year == now.getYear() && this.month == now.getMonthValue();
    }
}
