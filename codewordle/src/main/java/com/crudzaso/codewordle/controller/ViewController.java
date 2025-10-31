package com.crudzaso.codewordle.controller;

import com.crudzaso.codewordle.model.GameSession;
import com.crudzaso.codewordle.model.GuessValidation;
import com.crudzaso.codewordle.model.Theme;
import com.crudzaso.codewordle.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        // Get game history
        var gameHistory = gameService.getGameHistory(gameSessionId);
        model.addAttribute("attempts", gameHistory);

        // Get available themes for navigation
        List<Theme> themes = gameService.getAllThemes();
        model.addAttribute("themes", themes);

        return "game";
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