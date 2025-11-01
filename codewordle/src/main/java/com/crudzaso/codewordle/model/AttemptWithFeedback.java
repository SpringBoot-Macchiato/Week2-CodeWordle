package com.crudzaso.codewordle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptWithFeedback {
    private Long id;
    private Long gameSessionId;
    private String guessWord;
    private Integer attemptNumber;
    private List<LetterFeedback> feedback;
    private LocalDateTime createdAt;

    public AttemptWithFeedback(Attempt attempt, List<LetterFeedback> feedback) {
        this.id = attempt.getId();
        this.gameSessionId = attempt.getGameSessionId();
        this.guessWord = attempt.getGuessWord();
        this.attemptNumber = attempt.getAttemptNumber();
        this.feedback = feedback;
        this.createdAt = attempt.getCreatedAt();
    }
}