package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.Attempt;

import java.util.List;

public interface AttemptRepository {
    Attempt save(Attempt attempt);
    List<Attempt> findByGameSessionId(Long gameSessionId);
    int countByGameSessionId(Long gameSessionId);
}