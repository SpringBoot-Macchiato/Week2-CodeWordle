package com.crudzaso.codewordle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    private Long id;
    private String word;
    private Long themeId;

    public Word(String word, Long themeId) {
        this.word = word;
        this.themeId = themeId;
    }
}