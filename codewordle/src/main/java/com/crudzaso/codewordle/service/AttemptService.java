package com.crudzaso.codewordle.service;

import com.crudzaso.codewordle.model.Attempt;
import com.crudzaso.codewordle.repository.AttemptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepository;

    public AttemptService(AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    public int getNextAttemptNumber(Long gameSessionId) {
        return attemptRepository.countByGameSessionId(gameSessionId) + 1;
    }

    public void saveAttempt(Long gameSessionId, String guess, int attemptNumber, String feedback) {
        Attempt attempt = new Attempt(gameSessionId, guess, attemptNumber, feedback);
        attempt.setCreatedAt(LocalDateTime.now());
        attemptRepository.save(attempt);
    }

    public List<Attempt> getGameHistory(Long gameSessionId) {
        return attemptRepository.findByGameSessionId(gameSessionId);
    }
}