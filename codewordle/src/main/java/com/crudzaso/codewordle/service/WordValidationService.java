package com.crudzaso.codewordle.service;

import com.crudzaso.codewordle.model.LetterFeedback;
import com.crudzaso.codewordle.model.LetterStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordValidationService {

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

    public boolean isValidGuess(String guess, String targetWord, List<String> validWords) {
        if (guess == null || targetWord == null) {
            return false;
        }

        if (guess.length() != targetWord.length()) {
            return false;
        }

        return validWords.stream()
                .anyMatch(word -> word.equalsIgnoreCase(guess));
    }
}