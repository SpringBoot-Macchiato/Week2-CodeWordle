package com.crudzaso.codewordle.service;

import com.crudzaso.codewordle.model.LetterFeedback;
import com.crudzaso.codewordle.model.LetterStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for word validation and feedback generation
 */
@Service
public class WordValidationService {

    /**
     * Generate letter-by-letter feedback for a guess
     *
     * @param guess The word guess
     * @param targetWord The target word to compare against
     * @return List of letter feedback with status (CORRECT, PRESENT, ABSENT)
     */
    public List<LetterFeedback> generateFeedback(String guess, String targetWord) {
        char[] guessChars = guess.toLowerCase().toCharArray();
        char[] targetChars = targetWord.toLowerCase().toCharArray();
        LetterFeedback[] feedback = new LetterFeedback[guessChars.length];

        // First pass: mark correct positions
        boolean[] targetUsed = new boolean[targetChars.length];
        for (int i = 0; i < guessChars.length; i++) {
            if (guessChars[i] == targetChars[i]) {
                feedback[i] = new LetterFeedback(guessChars[i], LetterStatus.CORRECT, i);
                targetUsed[i] = true;
            }
        }

        // Second pass: mark present letters
        for (int i = 0; i < guessChars.length; i++) {
            if (feedback[i] != null) continue;

            for (int j = 0; j < targetChars.length; j++) {
                if (!targetUsed[j] && guessChars[i] == targetChars[j]) {
                    feedback[i] = new LetterFeedback(guessChars[i], LetterStatus.PRESENT, i);
                    targetUsed[j] = true;
                    break;
                }
            }

            if (feedback[i] == null) {
                feedback[i] = new LetterFeedback(guessChars[i], LetterStatus.ABSENT, i);
            }
        }

        return List.of(feedback);
    }

    /**
     * Check if a guess is valid for the game
     * Now accepts any 5-letter word (not restricted to theme words)
     *
     * @param guess The word to validate
     * @param targetWord The target word (for length comparison)
     * @param validWords List of valid words for the theme (not used anymore but kept for compatibility)
     * @return true if guess is valid, false otherwise
     */
    public boolean isValidGuess(String guess, String targetWord, List<String> validWords) {
        if (guess == null || targetWord == null) {
            return false;
        }

        if (guess.length() != targetWord.length()) {
            return false;
        }

        // Validate that guess contains only letters (A-Z)
        // Accept any 5-letter word in English alphabet
        return guess.matches("^[a-zA-Z]+$");
    }
}