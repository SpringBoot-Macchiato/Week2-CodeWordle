package com.crudzaso.codewordle.controller;

import com.crudzaso.codewordle.model.GameSession;
import com.crudzaso.codewordle.model.GuessValidation;
import com.crudzaso.codewordle.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for game operations
 */
@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Start a new game with the specified theme
     *
     * @param themeId ID of the theme to use
     * @return New game session
     */
    @PostMapping
    public ResponseEntity<GameSession> startNewGame(@RequestParam Long themeId) {
        try {
            GameSession gameSession = gameService.startNewGame(themeId);
            return ResponseEntity.ok(gameSession);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Submit a guess for a game session
     *
     * @param gameSessionId ID of the game session
     * @param guess The word guess to validate
     * @return Validation result with feedback
     */
    @PostMapping("/{gameSessionId}/attempts")
    public ResponseEntity<GuessValidation> submitGuess(
            @PathVariable Long gameSessionId,
            @RequestParam String guess) {

        GuessValidation validation = gameService.validateGuess(gameSessionId, guess);

        if (validation.isValid()) {
            return ResponseEntity.ok(validation);
        } else {
            return ResponseEntity.badRequest().body(validation);
        }
    }

    /**
     * Get game session details
     *
     * @param gameSessionId ID of the game session
     * @return Game session if found
     */
    @GetMapping("/{gameSessionId}")
    public ResponseEntity<GameSession> getGameSession(@PathVariable Long gameSessionId) {
        Optional<GameSession> gameSession = gameService.getGameSession(gameSessionId);
        return gameSession.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
}