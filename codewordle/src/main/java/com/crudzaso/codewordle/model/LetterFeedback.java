package com.crudzaso.codewordle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LetterFeedback {
    private char letter;
    private LetterStatus status;
    private int position;
}