package com.crudzaso.codewordle.service;

import com.crudzaso.codewordle.model.GameSession;
import com.crudzaso.codewordle.repository.GameSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;

    public GameSessionService(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
    }

    public GameSession save(GameSession gameSession) {
        return gameSessionRepository.save(gameSession);
    }

    public Optional<GameSession> getGameSession(Long gameSessionId) {
        return gameSessionRepository.findById(gameSessionId);
    }

    public void markGameAsWon(Long gameSessionId) {
        gameSessionRepository.updateStatus(gameSessionId, GameSession.GameStatus.WON.name());
    }

    public void markGameAsLost(Long gameSessionId) {
        gameSessionRepository.updateStatus(gameSessionId, GameSession.GameStatus.LOST.name());
    }

    public boolean isGameInProgress(GameSession gameSession) {
        return gameSession.getStatus() == GameSession.GameStatus.IN_PROGRESS;
    }
}