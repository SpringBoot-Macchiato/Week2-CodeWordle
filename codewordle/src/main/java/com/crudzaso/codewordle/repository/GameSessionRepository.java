package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.GameSession;

import java.util.List;
import java.util.Optional;

public interface GameSessionRepository {
    GameSession save(GameSession gameSession);
    Optional<GameSession> findById(Long id);
    void updateStatus(Long id, String status);
    List<GameSession> findActiveSessions();
}