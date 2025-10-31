package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> themeRowMapper;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.themeRowMapper = new ThemeRowMapper();
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT id, name, description FROM themes";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description FROM themes WHERE id = ?";
        List<Theme> themes = jdbcTemplate.query(sql, themeRowMapper, id);
        return themes.stream().findFirst();
    }

    @Override
    public Optional<Theme> findByName(String name) {
        String sql = "SELECT id, name, description FROM themes WHERE name = ?";
        List<Theme> themes = jdbcTemplate.query(sql, themeRowMapper, name);
        return themes.stream().findFirst();
    }
}