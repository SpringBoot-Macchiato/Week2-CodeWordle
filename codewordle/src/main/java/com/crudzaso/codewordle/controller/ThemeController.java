package com.crudzaso.codewordle.controller;

import com.crudzaso.codewordle.model.Theme;
import com.crudzaso.codewordle.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing game themes
 */
@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private final GameService gameService;

    public ThemeController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Get all available themes for the game
     *
     * @return List of all themes
     */
    @GetMapping
    public List<Theme> getAllThemes() {
        return gameService.getAllThemes();
    }
}