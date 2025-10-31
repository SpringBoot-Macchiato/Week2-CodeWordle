package com.crudzaso.codewordle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attempt {
    private Long id;
    private Long gameSessionId;
    private String guessWord;
    private Integer attemptNumber;
    private String feedback;
    private LocalDateTime createdAt;

    public Attempt(Long gameSessionId, String guessWord, Integer attemptNumber, String feedback) {
        this.gameSessionId = gameSessionId;
        this.guessWord = guessWord;
        this.attemptNumber = attemptNumber;
        this.feedback = feedback;
    }
}