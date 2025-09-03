package com.pena.app.repository;

import com.pena.app.entity.BusPass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusPassRepository extends JpaRepository<BusPass, Long> {
    Optional<BusPass> findByCode(String code);
}
