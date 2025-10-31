package com.crudzaso.codewordle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {
    private Long id;
    private Long targetWordId;
    private Long themeId;
    private GameStatus status;
    private LocalDateTime createdAt;

    public enum GameStatus {
        IN_PROGRESS, WON, LOST
    }
}