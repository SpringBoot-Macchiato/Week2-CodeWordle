package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    List<Theme> findAll();
    Optional<Theme> findById(Long id);
    Optional<Theme> findByName(String name);
}