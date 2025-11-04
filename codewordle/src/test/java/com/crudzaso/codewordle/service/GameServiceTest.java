package com.crudzaso.codewordle.service;

import com.crudzaso.codewordle.model.*;
import com.crudzaso.codewordle.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para GameService
 */
@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private WordRepository wordRepository;

    @Mock
    private GameSessionService gameSessionService;

    @Mock
    private AttemptService attemptService;

    @Mock
    private WordValidationService wordValidationService;

    @InjectMocks
    private GameService gameService;

    private Theme testTheme;
    private Word testWord;
    private GameSession testGameSession;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        testTheme = new Theme();
        testTheme.setId(1L);
        testTheme.setName("JAVA");
        testTheme.setDescription("Java programming language terms");

        testWord = new Word();
        testWord.setId(1L);
        testWord.setWord("CLASS");
        testWord.setThemeId(1L);

        testGameSession = new GameSession();
        testGameSession.setId(1L);
        testGameSession.setTargetWordId(1L);
        testGameSession.setThemeId(1L);
        testGameSession.setStatus(GameSession.GameStatus.IN_PROGRESS);
        testGameSession.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAllThemes_ReturnsThemesList() {
        // Arrange
        List<Theme> expectedThemes = List.of(testTheme);
        when(themeRepository.findAll()).thenReturn(expectedThemes);

        // Act
        List<Theme> result = gameService.getAllThemes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("JAVA", result.get(0).getName());
        verify(themeRepository, times(1)).findAll();
    }

    @Test
    void testStartNewGame_ValidThemeId_ReturnsGameSession() {
        // Arrange
        when(wordRepository.findRandomByThemeId(1L)).thenReturn(Optional.of(testWord));
        when(gameSessionService.save(any(GameSession.class))).thenAnswer(invocation -> {
            GameSession session = invocation.getArgument(0);
            session.setId(1L);
            return session;
        });

        // Act
        GameSession result = gameService.startNewGame(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getTargetWordId());
        assertEquals(1L, result.getThemeId());
        assertEquals(GameSession.GameStatus.IN_PROGRESS, result.getStatus());
        assertNotNull(result.getCreatedAt());
        verify(wordRepository, times(1)).findRandomByThemeId(1L);
        verify(gameSessionService, times(1)).save(any(GameSession.class));
    }

    @Test
    void testStartNewGame_NoWordsAvailable_ThrowsException() {
        // Arrange
        when(wordRepository.findRandomByThemeId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> gameService.startNewGame(1L)
        );

        assertEquals("No words available for theme: 1", exception.getMessage());
        verify(wordRepository, times(1)).findRandomByThemeId(1L);
        verify(gameSessionService, never()).save(any(GameSession.class));
    }

    @Test
    void testValidateGuess_GameSessionNotFound_ReturnsInvalidValidation() {
        // Arrange
        when(gameSessionService.getGameSession(1L)).thenReturn(Optional.empty());

        // Act
        GuessValidation result = gameService.validateGuess(1L, "CLASS");

        // Assert
        assertFalse(result.isValid());
        assertEquals("Game session not found", result.getMessage());
        assertNull(result.getFeedback());
        assertFalse(result.isGameWon());
        assertFalse(result.isGameOver());
        verify(gameSessionService, times(1)).getGameSession(1L);
    }

    @Test
    void testValidateGuess_GameAlreadyCompleted_ReturnsInvalidValidation() {
        // Arrange
        testGameSession.setStatus(GameSession.GameStatus.WON);
        when(gameSessionService.getGameSession(1L)).thenReturn(Optional.of(testGameSession));
        when(gameSessionService.isGameInProgress(testGameSession)).thenReturn(false);

        // Act
        GuessValidation result = gameService.validateGuess(1L, "CLASS");

        // Assert
        assertFalse(result.isValid());
        assertEquals("Game is already completed", result.getMessage());
        verify(gameSessionService, times(1)).getGameSession(1L);
        verify(gameSessionService, times(1)).isGameInProgress(testGameSession);
    }

    @Test
    void testValidateGuess_TargetWordNotFound_ReturnsInvalidValidation() {
        // Arrange
        when(gameSessionService.getGameSession(1L)).thenReturn(Optional.of(testGameSession));
        when(gameSessionService.isGameInProgress(testGameSession)).thenReturn(true);
        when(wordRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        GuessValidation result = gameService.validateGuess(1L, "CLASS");

        // Assert
        assertFalse(result.isValid());
        assertEquals("Target word not found", result.getMessage());
        verify(wordRepository, times(1)).findById(1L);
    }

    @Test
    void testValidateGuess_InvalidLength_ReturnsInvalidValidation() {
        // Arrange
        when(gameSessionService.getGameSession(1L)).thenReturn(Optional.of(testGameSession));
        when(gameSessionService.isGameInProgress(testGameSession)).thenReturn(true);
        when(wordRepository.findById(1L)).thenReturn(Optional.of(testWord));

        // Act
        GuessValidation result = gameService.validateGuess(1L, "CLAS"); // 4 letras en lugar de 5

        // Assert
        assertFalse(result.isValid());
        assertEquals("Guess must be 5 letters long", result.getMessage());
        verify(wordRepository, times(1)).findById(1L);
    }

    @Test
    void testValidateGuess_InvalidWord_ReturnsInvalidValidation() {
        // Arrange - Test with invalid characters (not A-Z)
        when(gameSessionService.getGameSession(1L)).thenReturn(Optional.of(testGameSession));
        when(gameSessionService.isGameInProgress(testGameSession)).thenReturn(true);
        when(wordRepository.findById(1L)).thenReturn(Optional.of(testWord));
        when(wordRepository.findByThemeId(1L)).thenReturn(List.of(testWord));
        when(wordValidationService.isValidGuess("123AB", "CLASS", List.of("CLASS"))).thenReturn(false);

        // Act
        GuessValidation result = gameService.validateGuess(1L, "123AB");

        // Assert
        assertFalse(result.isValid());
        assertEquals("Invalid word", result.getMessage());
        verify(wordValidationService, times(1)).isValidGuess("123AB", "CLASS", List.of("CLASS"));
    }

    @Test
    void testValidateGuess_ValidGuess_ReturnsSuccessValidation() {
        // Arrange
        List<LetterFeedback> expectedFeedback = List.of(
            new LetterFeedback('C', LetterStatus.CORRECT, 0),
            new LetterFeedback('L', LetterStatus.CORRECT, 1),
            new LetterFeedback('A', LetterStatus.CORRECT, 2),
            new LetterFeedback('S', LetterStatus.CORRECT, 3),
            new LetterFeedback('S', LetterStatus.CORRECT, 4)
        );

        when(gameSessionService.getGameSession(1L)).thenReturn(Optional.of(testGameSession));
        when(gameSessionService.isGameInProgress(testGameSession)).thenReturn(true);
        when(wordRepository.findById(1L)).thenReturn(Optional.of(testWord));
        when(wordRepository.findByThemeId(1L)).thenReturn(List.of(testWord));
        when(wordValidationService.isValidGuess("CLASS", "CLASS", List.of("CLASS"))).thenReturn(true);
        when(wordValidationService.generateFeedback("CLASS", "CLASS")).thenReturn(expectedFeedback);
        when(attemptService.getNextAttemptNumber(1L)).thenReturn(1);
        doNothing().when(attemptService).saveAttempt(anyLong(), anyString(), anyInt(), anyString());

        // Act
        GuessValidation result = gameService.validateGuess(1L, "CLASS");

        // Assert
        assertTrue(result.isValid());
        assertEquals("Valid guess", result.getMessage());
        assertNotNull(result.getFeedback());
        assertEquals(5, result.getFeedback().size());
        assertTrue(result.isGameWon());
        assertTrue(result.isGameOver());

        verify(wordValidationService, times(1)).isValidGuess("CLASS", "CLASS", List.of("CLASS"));
        verify(wordValidationService, times(1)).generateFeedback("CLASS", "CLASS");
        verify(attemptService, times(1)).getNextAttemptNumber(1L);
        verify(attemptService, times(1)).saveAttempt(eq(1L), eq("CLASS"), eq(1), anyString());
        verify(gameSessionService, times(1)).markGameAsWon(1L);
    }

    @Test
    void testValidateGuess_ValidGuessButNotWinning_ReturnsSuccessValidation() {
        // Arrange
        List<LetterFeedback> expectedFeedback = List.of(
            new LetterFeedback('A', LetterStatus.PRESENT, 0),
            new LetterFeedback('R', LetterStatus.PRESENT, 1),
            new LetterFeedback('R', LetterStatus.PRESENT, 2),
            new LetterFeedback('A', LetterStatus.PRESENT, 3),
            new LetterFeedback('Y', LetterStatus.ABSENT, 4)
        );

        when(gameSessionService.getGameSession(1L)).thenReturn(Optional.of(testGameSession));
        when(gameSessionService.isGameInProgress(testGameSession)).thenReturn(true);
        when(wordRepository.findById(1L)).thenReturn(Optional.of(testWord));
        when(wordRepository.findByThemeId(1L)).thenReturn(List.of(testWord));
        when(wordValidationService.isValidGuess("ARRAY", "CLASS", List.of("CLASS"))).thenReturn(true);
        when(wordValidationService.generateFeedback("ARRAY", "CLASS")).thenReturn(expectedFeedback);
        when(attemptService.getNextAttemptNumber(1L)).thenReturn(1);
        doNothing().when(attemptService).saveAttempt(anyLong(), anyString(), anyInt(), anyString());

        // Act
        GuessValidation result = gameService.validateGuess(1L, "ARRAY");

        // Assert
        assertTrue(result.isValid());
        assertEquals("Valid guess", result.getMessage());
        assertNotNull(result.getFeedback());
        assertEquals(5, result.getFeedback().size());
        assertFalse(result.isGameWon());
        assertFalse(result.isGameOver());

        verify(gameSessionService, never()).markGameAsWon(anyLong());
        verify(gameSessionService, never()).markGameAsLost(anyLong());
    }

    @Test
    void testValidateGuess_LastAttemptGameLost_ReturnsSuccessValidation() {
        // Arrange
        List<LetterFeedback> expectedFeedback = List.of(
            new LetterFeedback('F', LetterStatus.ABSENT, 0),
            new LetterFeedback('I', LetterStatus.ABSENT, 1),
            new LetterFeedback('E', LetterStatus.ABSENT, 2),
            new LetterFeedback('L', LetterStatus.ABSENT, 3),
            new LetterFeedback('D', LetterStatus.ABSENT, 4)
        );

        when(gameSessionService.getGameSession(1L)).thenReturn(Optional.of(testGameSession));
        when(gameSessionService.isGameInProgress(testGameSession)).thenReturn(true);
        when(wordRepository.findById(1L)).thenReturn(Optional.of(testWord));
        when(wordRepository.findByThemeId(1L)).thenReturn(List.of(testWord));
        when(wordValidationService.isValidGuess("FIELD", "CLASS", List.of("CLASS"))).thenReturn(true);
        when(wordValidationService.generateFeedback("FIELD", "CLASS")).thenReturn(expectedFeedback);
        when(attemptService.getNextAttemptNumber(1L)).thenReturn(6); // Último intento
        doNothing().when(attemptService).saveAttempt(anyLong(), anyString(), anyInt(), anyString());

        // Act
        GuessValidation result = gameService.validateGuess(1L, "FIELD");

        // Assert
        assertTrue(result.isValid());
        assertEquals("Valid guess", result.getMessage());
        assertNotNull(result.getFeedback());
        assertFalse(result.isGameWon());
        assertTrue(result.isGameOver());

        verify(gameSessionService, times(1)).markGameAsLost(1L);
    }

    @Test
    void testGetGameHistory_ReturnsAttemptsList() {
        // Arrange
        List<Attempt> expectedAttempts = List.of(new Attempt());
        when(attemptService.getGameHistory(1L)).thenReturn(expectedAttempts);

        // Act
        List<Attempt> result = gameService.getGameHistory(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(attemptService, times(1)).getGameHistory(1L);
    }

    @Test
    void testGetGameSession_ReturnsGameSession() {
        // Arrange
        when(gameSessionService.getGameSession(1L)).thenReturn(Optional.of(testGameSession));

        // Act
        Optional<GameSession> result = gameService.getGameSession(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(gameSessionService, times(1)).getGameSession(1L);
    }

    @Test
    void testGetGameSession_NotFound_ReturnsEmpty() {
        // Arrange
        when(gameSessionService.getGameSession(1L)).thenReturn(Optional.empty());

        // Act
        Optional<GameSession> result = gameService.getGameSession(1L);

        // Assert
        assertTrue(result.isEmpty());
        verify(gameSessionService, times(1)).getGameSession(1L);
    }
}