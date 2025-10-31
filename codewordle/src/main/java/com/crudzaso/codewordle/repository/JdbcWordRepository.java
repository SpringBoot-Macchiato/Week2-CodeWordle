package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.Word;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcWordRepository implements WordRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Word> wordRowMapper;

    public JdbcWordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.wordRowMapper = new WordRowMapper();
    }

    @Override
    public List<Word> findAll() {
        String sql = "SELECT id, word, theme_id FROM words";
        return jdbcTemplate.query(sql, wordRowMapper);
    }

    @Override
    public Optional<Word> findById(Long id) {
        String sql = "SELECT id, word, theme_id FROM words WHERE id = ?";
        List<Word> words = jdbcTemplate.query(sql, wordRowMapper, id);
        return words.stream().findFirst();
    }

    @Override
    public List<Word> findByThemeId(Long themeId) {
        String sql = "SELECT id, word, theme_id FROM words WHERE theme_id = ?";
        return jdbcTemplate.query(sql, wordRowMapper, themeId);
    }

    @Override
    public Optional<Word> findRandomByThemeId(Long themeId) {
        String sql = "SELECT id, word, theme_id FROM words WHERE theme_id = ? ORDER BY RAND() LIMIT 1";
        List<Word> words = jdbcTemplate.query(sql, wordRowMapper, themeId);
        return words.stream().findFirst();
    }
}