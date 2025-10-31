package com.crudzaso.codewordle.service;

import com.crudzaso.codewordle.model.*;
import com.crudzaso.codewordle.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {

    private static final int MAX_ATTEMPTS = 6;

    private final ThemeRepository themeRepository;
    private final WordRepository wordRepository;
    private final GameSessionService gameSessionService;
    private final AttemptService attemptService;
    private final WordValidationService wordValidationService;

    public GameService(ThemeRepository themeRepository,
                      WordRepository wordRepository,
                      GameSessionService gameSessionService,
                      AttemptService attemptService,
                      WordValidationService wordValidationService) {
        this.themeRepository = themeRepository;
        this.wordRepository = wordRepository;
        this.gameSessionService = gameSessionService;
        this.attemptService = attemptService;
        this.wordValidationService = wordValidationService;
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    public GameSession startNewGame(Long themeId) {
        Optional<Word> randomWord = wordRepository.findRandomByThemeId(themeId);
        if (randomWord.isEmpty()) {
            throw new IllegalArgumentException("No words available for theme: " + themeId);
        }

        GameSession gameSession = new GameSession();
        gameSession.setTargetWordId(randomWord.get().getId());
        gameSession.setThemeId(themeId);
        gameSession.setStatus(GameSession.GameStatus.IN_PROGRESS);
        gameSession.setCreatedAt(LocalDateTime.now());

        return gameSessionService.save(gameSession);
    }

    public GuessValidation validateGuess(Long gameSessionId, String guess) {
        Optional<GameSession> gameSessionOpt = gameSessionService.getGameSession(gameSessionId);
        if (gameSessionOpt.isEmpty()) {
            return createInvalidValidation("Game session not found");
        }

        GameSession gameSession = gameSessionOpt.get();
        if (!gameSessionService.isGameInProgress(gameSession)) {
            return createInvalidValidation("Game is already completed");
        }

        // Get target word
        Optional<Word> targetWordOpt = wordRepository.findById(gameSession.getTargetWordId());
        if (targetWordOpt.isEmpty()) {
            return createInvalidValidation("Target word not found");
        }

        String targetWord = targetWordOpt.get().getWord();

        // Validate guess length
        if (guess.length() != targetWord.length()) {
            return createInvalidValidation("Guess must be " + targetWord.length() + " letters long");
        }

        // Check if word exists in theme
        List<Word> themeWords = wordRepository.findByThemeId(gameSession.getThemeId());
        List<String> validWords = themeWords.stream()
                .map(Word::getWord)
                .collect(Collectors.toList());

        if (!wordValidationService.isValidGuess(guess, targetWord, validWords)) {
            return createInvalidValidation("Word not found in theme");
        }

        // Generate feedback
        List<LetterFeedback> feedback = wordValidationService.generateFeedback(guess, targetWord);

        // Save attempt
        int attemptNumber = attemptService.getNextAttemptNumber(gameSessionId);
        attemptService.saveAttempt(gameSessionId, guess, attemptNumber, feedback.toString());

        // Check game status
        boolean gameWon = guess.equalsIgnoreCase(targetWord);
        boolean gameOver = attemptNumber >= MAX_ATTEMPTS || gameWon;

        if (gameWon) {
            gameSessionService.markGameAsWon(gameSessionId);
        } else if (gameOver) {
            gameSessionService.markGameAsLost(gameSessionId);
        }

        return new GuessValidation(true, "Valid guess", feedback, gameWon, gameOver);
    }


    private GuessValidation createInvalidValidation(String message) {
        return new GuessValidation(false, message, null, false, false);
    }

    public List<Attempt> getGameHistory(Long gameSessionId) {return attemptService.getGameHistory(gameSessionId);
    }

    public Optional<GameSession> getGameSession(Long gameSessionId) {
        return gameSessionService.getGameSession(gameSessionId);
    }
}