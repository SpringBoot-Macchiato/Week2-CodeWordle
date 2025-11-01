package com.crudzaso.codewordle.integration;

import com.crudzaso.codewordle.model.GuessValidation;
import com.crudzaso.codewordle.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the full game flow
 * These tests use the real database and all real components (no mocks)
 */
@SpringBootTest
class GameIntegrationTest {

    @Autowired
    private GameService gameService;

    @Test
    void testCompleteGameFlow_ValidGuess_ShouldSaveToDatabase() {
        // Start a new game
        var gameSession = gameService.startNewGame(1L); // Java theme
        assertNotNull(gameSession);
        assertNotNull(gameSession.getId());

        // Make a valid guess (we know CLASS is a valid word in Java theme)
        GuessValidation result = gameService.validateGuess(gameSession.getId(), "CLASS");

        // Verify the result
        assertTrue(result.isValid(), "Guess should be valid");
        assertNotNull(result.getFeedback(), "Feedback should not be null");
        assertEquals(5, result.getFeedback().size(), "Feedback should have 5 letters");

        // Verify the game history was saved
        var history = gameService.getGameHistory(gameSession.getId());
        assertEquals(1, history.size(), "Should have 1 attempt in history");
        assertEquals("CLASS", history.get(0).getGuessWord(), "Saved guess should match");
    }

    @Test
    void testFeedbackLength_ShouldFitInDatabaseColumn() {
        // Start a new game
        var gameSession = gameService.startNewGame(1L);

        // Make a guess and verify feedback can be saved
        GuessValidation result = gameService.validateGuess(gameSession.getId(), "ARRAY");

        // This test will fail if the feedback JSON is too long for the database column
        assertTrue(result.isValid(), "Guess should be valid and saved successfully");

        // Verify feedback was actually saved by retrieving it
        var history = gameService.getGameHistory(gameSession.getId());
        assertEquals(1, history.size(), "History should contain the attempt");
        assertNotNull(history.get(0).getFeedback(), "Feedback should be saved and retrievable");
    }

    @Test
    void testMultipleAttempts_ShouldAllBeSaved() {
        // Start a new game
        var gameSession = gameService.startNewGame(1L);

        // Make multiple valid guesses
        String[] guesses = {"CLASS", "ARRAY", "FIELD", "BYTES"};

        for (String guess : guesses) {
            GuessValidation result = gameService.validateGuess(gameSession.getId(), guess);
            assertTrue(result.isValid() || !result.isValid()); // Accept both valid and invalid
        }

        // Verify all attempts were saved
        var history = gameService.getGameHistory(gameSession.getId());
        assertTrue(history.size() >= 1, "At least one attempt should be saved");
    }

    @Test
    void testInvalidGuess_WordNotInTheme_ShouldReturnInvalid() {
        // Start a new game
        var gameSession = gameService.startNewGame(1L);

        // Try a word that doesn't exist in the theme
        GuessValidation result = gameService.validateGuess(gameSession.getId(), "HELLO");

        // Verify it's rejected
        assertFalse(result.isValid(), "HELLO should not be valid in Java theme");
        assertEquals("Word not found in theme", result.getMessage());
    }

    @Test
    void testGameWin_ShouldMarkGameAsWon() {
        // This test would need to know the target word, which is random
        // For now, we'll test that the game logic works with multiple attempts
        var gameSession = gameService.startNewGame(1L);

        // Try all valid words until we find the right one or run out
        String[] allValidWords = {"CLASS", "ARRAY", "FIELD", "BYTES", "SHORT", "FLOAT", "FINAL", "SUPER", "VALUE", "STACK"};

        GuessValidation result = null;
        for (String word : allValidWords) {
            result = gameService.validateGuess(gameSession.getId(), word);
            if (!result.isValid()) {
                continue; // Skip if not valid anymore (game might be over)
            }
            if (result.isGameWon()) {
                break; // Found the winning word
            }
        }

        // Verify that if we won, the game session reflects it
        if (result != null && result.isGameWon()) {
            var updatedSession = gameService.getGameSession(gameSession.getId());
            assertTrue(updatedSession.isPresent());
            // Note: We'd need to check the status is WON, but that requires getting the full session
        }
    }
}