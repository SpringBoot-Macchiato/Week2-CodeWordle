package com.crudzaso.codewordle.controller;

import com.crudzaso.codewordle.model.*;
import com.crudzaso.codewordle.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * MVC Controller for web views
 */
@Controller
@RequestMapping("/")
public class ViewController {

    private final GameService gameService;

    public ViewController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Home page - show theme selection
     *
     * @param model Model for view data
     * @return Home page view
     */
    @GetMapping
    public String home(Model model) {
        List<Theme> themes = gameService.getAllThemes();
        model.addAttribute("themes", themes);
        return "home";
    }

    /**
     * Start a new game and redirect to game page
     *
     * @param themeId Selected theme ID
     * @return Redirect to game page
     */
    @PostMapping("/start-game")
    public String startGame(@RequestParam Long themeId) {
        GameSession gameSession = gameService.startNewGame(themeId);
        return "redirect:/game/" + gameSession.getId();
    }

    /**
     * Game page - show game interface
     *
     * @param gameSessionId Game session ID
     * @param model Model for view data
     * @return Game page view
     */
    @GetMapping("/game/{gameSessionId}")
    public String game(@PathVariable Long gameSessionId, Model model) {
        // Get game session details
        var gameSessionOpt = gameService.getGameSession(gameSessionId);
        if (gameSessionOpt.isEmpty()) {
            return "redirect:/";
        }

        GameSession gameSession = gameSessionOpt.get();
        model.addAttribute("gameSession", gameSession);

        // Get game history and parse feedback
        var gameHistory = gameService.getGameHistory(gameSessionId);
        List<AttemptWithFeedback> attemptsWithFeedback = parseFeedbackFromAttempts(gameHistory);
        model.addAttribute("attempts", attemptsWithFeedback);

        // Get available themes for navigation
        List<Theme> themes = gameService.getAllThemes();
        model.addAttribute("themes", themes);

        // Get target word length for dynamic UI
        int wordLength = getTargetWordLength(gameSession.getTargetWordId());
        model.addAttribute("wordLength", wordLength);

        return "game";
    }

    /**
     * Get the length of the target word
     *
     * @param targetWordId ID of the target word
     * @return Length of the target word
     */
    private int getTargetWordLength(Long targetWordId) {
        // Since all words are now standardized to 5 letters, return 5
        return 5;
    }

    /**
     * Parse feedback JSON from attempts and convert to AttemptWithFeedback objects
     *
     * @param attempts List of attempts with JSON feedback
     * @return List of AttemptWithFeedback with parsed feedback
     */
    private List<AttemptWithFeedback> parseFeedbackFromAttempts(List<Attempt> attempts) {
        List<AttemptWithFeedback> result = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Attempt attempt : attempts) {
            try {
                // Parse JSON feedback string to List<LetterFeedback>
                String feedbackJson = attempt.getFeedback();
                List<LetterFeedback> feedback = objectMapper.readValue(
                    feedbackJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, LetterFeedback.class)
                );

                result.add(new AttemptWithFeedback(attempt, feedback));
            } catch (Exception e) {
                // If parsing fails, create empty feedback
                System.err.println("Error parsing feedback for attempt " + attempt.getId() + ": " + e.getMessage());
                result.add(new AttemptWithFeedback(attempt, new ArrayList<>()));
            }
        }

        return result;
    }

    /**
     * Submit a guess via AJAX
     *
     * @param gameSessionId Game session ID
     * @param guess Word guess
     * @return Validation result
     */
    @PostMapping("/game/{gameSessionId}/guess")
    @ResponseBody
    public GuessValidation submitGuess(
            @PathVariable Long gameSessionId,
            @RequestParam String guess) {
        return gameService.validateGuess(gameSessionId, guess);
    }
}