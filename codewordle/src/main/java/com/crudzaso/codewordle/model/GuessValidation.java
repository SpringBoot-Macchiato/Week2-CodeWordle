package com.crudzaso.codewordle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuessValidation {
    private boolean valid;
    private String message;
    private List<LetterFeedback> feedback;
    private boolean gameWon;
    private boolean gameOver;
}