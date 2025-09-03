package com.pena.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MatchGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String opponent;

    private LocalDate matchDate;

    private String location;

    public MatchGame() {
    }

    public MatchGame(String opponent, LocalDate matchDate, String location) {
        this.opponent = opponent;
        this.matchDate = matchDate;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public String getOpponent() {
        return opponent;
    }

    public MatchGame setOpponent(String opponent) {
        this.opponent = opponent;
        return this;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public MatchGame setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MatchGame setLocation(String location) {
        this.location = location;
        return this;
    }
}
