package com.crudzaso.codewordle.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para WordValidationService
 */
@ExtendWith(MockitoExtension.class)
class WordValidationServiceTest {

    @InjectMocks
    private WordValidationService wordValidationService;

    @BeforeEach
    void setUp() {
        // El servicio se inyecta automáticamente con @InjectMocks
    }

    @Test
    void testIsValidGuess_ValidWord_ReturnsTrue() {
        // Arrange
        String guess = "CLASS";
        String targetWord = "CLASS";
        List<String> validWords = List.of("CLASS", "ARRAY", "FIELD");

        // Act
        boolean result = wordValidationService.isValidGuess(guess, targetWord, validWords);

        // Assert
        assertTrue(result, "Palabra válida debería retornar true");
    }

    @Test
    void testIsValidGuess_WordWithNumbers_ReturnsFalse() {
        // Arrange
        String guess = "CL4SS";
        String targetWord = "CLASS";
        List<String> validWords = List.of("CLASS", "ARRAY", "FIELD");

        // Act
        boolean result = wordValidationService.isValidGuess(guess, targetWord, validWords);

        // Assert
        assertFalse(result, "Palabra con números debería retornar false");
    }

    @Test
    void testIsValidGuess_WordWithSpecialCharacters_ReturnsFalse() {
        // Arrange
        String guess = "CL@SS";
        String targetWord = "CLASS";
        List<String> validWords = List.of("CLASS", "ARRAY", "FIELD");

        // Act
        boolean result = wordValidationService.isValidGuess(guess, targetWord, validWords);

        // Assert
        assertFalse(result, "Palabra con caracteres especiales debería retornar false");
    }

    @Test
    void testIsValidGuess_WordWithSpaces_ReturnsFalse() {
        // Arrange
        String guess = "CL ASS";
        String targetWord = "CLASS";
        List<String> validWords = List.of("CLASS", "ARRAY", "FIELD");

        // Act
        boolean result = wordValidationService.isValidGuess(guess, targetWord, validWords);

        // Assert
        assertFalse(result, "Palabra con espacios debería retornar false");
    }

    @Test
    void testIsValidGuess_EmptyString_ReturnsFalse() {
        // Arrange
        String guess = "";
        String targetWord = "CLASS";
        List<String> validWords = List.of("CLASS", "ARRAY", "FIELD");

        // Act
        boolean result = wordValidationService.isValidGuess(guess, targetWord, validWords);

        // Assert
        assertFalse(result, "String vacío debería retornar false");
    }

    @Test
    void testIsValidGuess_NullString_ReturnsFalse() {
        // Arrange
        String guess = null;
        String targetWord = "CLASS";
        List<String> validWords = List.of("CLASS", "ARRAY", "FIELD");

        // Act
        boolean result = wordValidationService.isValidGuess(guess, targetWord, validWords);

        // Assert
        assertFalse(result, "String nulo debería retornar false");
    }

    @Test
    void testIsValidGuess_LowercaseWord_ReturnsTrue() {
        // Arrange
        String guess = "class";
        String targetWord = "CLASS";
        List<String> validWords = List.of("CLASS", "ARRAY", "FIELD");

        // Act
        boolean result = wordValidationService.isValidGuess(guess, targetWord, validWords);

        // Assert
        assertTrue(result, "Palabra en minúsculas debería retornar true");
    }

    @Test
    void testIsValidGuess_MixedCaseWord_ReturnsTrue() {
        // Arrange
        String guess = "ClAsS";
        String targetWord = "CLASS";
        List<String> validWords = List.of("CLASS", "ARRAY", "FIELD");

        // Act
        boolean result = wordValidationService.isValidGuess(guess, targetWord, validWords);

        // Assert
        assertTrue(result, "Palabra con mezcla de mayúsculas y minúsculas debería retornar true");
    }

    @Test
    void testIsValidGuess_DifferentLength_ReturnsFalse() {
        // Arrange
        String guess = "CLAS";
        String targetWord = "CLASS";
        List<String> validWords = List.of("CLASS", "ARRAY", "FIELD");

        // Act
        boolean result = wordValidationService.isValidGuess(guess, targetWord, validWords);

        // Assert
        assertFalse(result, "Palabra con longitud diferente debería retornar false");
    }

    @Test
    void testIsValidGuess_WordNotInValidWords_ReturnsTrue() {
        // Arrange - Now accepts any 5-letter word
        String guess = "HELLO";
        String targetWord = "CLASS";
        List<String> validWords = List.of("CLASS", "ARRAY", "FIELD");

        // Act
        boolean result = wordValidationService.isValidGuess(guess, targetWord, validWords);

        // Assert
        assertTrue(result, "Any 5-letter word should be accepted");
    }
}