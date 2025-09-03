package com.pena.app.repository;

import com.pena.app.entity.MatchGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchGameRepository extends JpaRepository<MatchGame, Long> {
}
