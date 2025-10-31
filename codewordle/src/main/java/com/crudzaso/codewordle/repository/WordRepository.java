package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.Word;

import java.util.List;
import java.util.Optional;

public interface WordRepository {
    List<Word> findAll();
    Optional<Word> findById(Long id);
    List<Word> findByThemeId(Long themeId);
    Optional<Word> findRandomByThemeId(Long themeId);
}