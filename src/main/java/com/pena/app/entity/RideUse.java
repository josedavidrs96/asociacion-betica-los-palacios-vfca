package com.pena.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class RideUse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BusPass busPass;

    private LocalDateTime usedAt = LocalDateTime.now();

    public RideUse() {
    }

    public RideUse(BusPass busPass) {
        this.busPass = busPass;
    }

    public Long getId() {
        return id;
    }

    public BusPass getBusPass() {
        return busPass;
    }

    public RideUse setBusPass(BusPass busPass) {
        this.busPass = busPass;
        return this;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public RideUse setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
        return this;
    }
}
