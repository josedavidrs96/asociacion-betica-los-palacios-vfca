package com.pena.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BusTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busName;

    @ManyToOne(fetch = FetchType.LAZY)
    private MatchGame matchGame;

    private LocalDateTime departureTime;

    public BusTrip() {
    }

    public BusTrip(String busName, MatchGame matchGame, LocalDateTime departureTime) {
        this.busName = busName;
        this.matchGame = matchGame;
        this.departureTime = departureTime;
    }

    public Long getId() {
        return id;
    }

    public String getBusName() {
        return busName;
    }

    public BusTrip setBusName(String busName) {
        this.busName = busName;
        return this;
    }

    public MatchGame getMatchGame() {
        return matchGame;
    }

    public BusTrip setMatchGame(MatchGame matchGame) {
        this.matchGame = matchGame;
        return this;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public BusTrip setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
        return this;
    }
}
