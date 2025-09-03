package com.pena.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BusPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;

    @Column(unique = true, nullable = false, length = 64)
    private String code;

    private int totalUses = 5;

    private int usedCount = 0;

    // Nuevo: activo/inactivo
    @Column(nullable = false, columnDefinition = "INTEGER NOT NULL DEFAULT 1")
    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    public BusPass() {
    }

    public BusPass(Member member, String code) {
        this.member = member;
        this.code = code;
    }

    public Long getId() { return id; }

    public Member getMember() { return member; }

    public BusPass setMember(Member member) {
        this.member = member;
        return this;
    }

    public String getCode() { return code; }

    public BusPass setCode(String code) {
        this.code = code;
        return this;
    }

    public int getTotalUses() { return totalUses; }

    public BusPass setTotalUses(int totalUses) {
        this.totalUses = totalUses;
        return this;
    }

    public int getUsedCount() { return usedCount; }

    public BusPass setUsedCount(int usedCount) {
        this.usedCount = usedCount;
        return this;
    }

    public boolean isActive() { return active; }

    public BusPass setActive(boolean active) {
        this.active = active;
        return this;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public BusPass setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Transient
    public int getRemainingUses() {
        return Math.max(0, totalUses - usedCount);
    }

    public boolean canUse() {
        return active && getRemainingUses() > 0;
    }

    public void consumeOne() {
        if (canUse()) {
            this.usedCount++;
        }
    }
}
