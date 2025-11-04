package com.crudzaso.codewordle.service;

import com.crudzaso.codewordle.model.*;
import com.crudzaso.codewordle.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Main service for game operations and business logic
 */
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

    /**
     * Get all available themes
     *
     * @return List of all themes
     */
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    /**
     * Start a new game session with random word from theme
     *
     * @param themeId ID of the theme to use
     * @return New game session
     * @throws IllegalArgumentException if no words available for theme
     */
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

    /**
     * Validate a guess and provide feedback
     *
     * @param gameSessionId ID of the game session
     * @param guess The word to validate
     * @return Validation result with feedback and game status
     */
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

        // Validate the guess (accepts any 5-letter word)
        List<Word> themeWords = wordRepository.findByThemeId(gameSession.getThemeId());
        List<String> validWords = themeWords.stream()
                .map(Word::getWord)
                .collect(Collectors.toList());

        if (!wordValidationService.isValidGuess(guess, targetWord, validWords)) {
            return createInvalidValidation("Invalid word");
        }

        // Generate feedback
        List<LetterFeedback> feedback = wordValidationService.generateFeedback(guess, targetWord);

        // Save attempt
        int attemptNumber = attemptService.getNextAttemptNumber(gameSessionId);
        String feedbackJson = serializeFeedback(feedback);
        attemptService.saveAttempt(gameSessionId, guess, attemptNumber, feedbackJson);

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

    private String serializeFeedback(List<LetterFeedback> feedback) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < feedback.size(); i++) {
            LetterFeedback letter = feedback.get(i);
            json.append("{\"letter\":\"")
                .append(letter.getLetter())
                .append("\",\"status\":\"")
                .append(letter.getStatus())
                .append("\"}");
            if (i < feedback.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    public List<Attempt> getGameHistory(Long gameSessionId) {return attemptService.getGameHistory(gameSessionId);
    }

    public Optional<GameSession> getGameSession(Long gameSessionId) {
        return gameSessionService.getGameSession(gameSessionId);
    }
}