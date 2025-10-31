package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.GameSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcGameSessionRepository implements GameSessionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GameSession> gameSessionRowMapper;

    public JdbcGameSessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.gameSessionRowMapper = new GameSessionRowMapper();
    }

    @Override
    public GameSession save(GameSession gameSession) {
        if (gameSession.getId() == null) {
            return insert(gameSession);
        } else {
            update(gameSession);
            return gameSession;
        }
    }

    private GameSession insert(GameSession gameSession) {
        String sql = "INSERT INTO game_sessions (target_word_id, theme_id, status, created_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, gameSession.getTargetWordId());
            ps.setLong(2, gameSession.getThemeId());
            ps.setString(3, gameSession.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(gameSession.getCreatedAt()));
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKeyAs(Long.class);
        gameSession.setId(generatedId);
        return gameSession;
    }

    private void update(GameSession gameSession) {
        String sql = "UPDATE game_sessions SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, gameSession.getStatus().name(), gameSession.getId());
    }

    @Override
    public Optional<GameSession> findById(Long id) {
        String sql = "SELECT id, target_word_id, theme_id, status, created_at FROM game_sessions WHERE id = ?";
        List<GameSession> sessions = jdbcTemplate.query(sql, gameSessionRowMapper, id);
        return sessions.stream().findFirst();
    }

    @Override
    public void updateStatus(Long id, String status) {
        String sql = "UPDATE game_sessions SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }

    @Override
    public List<GameSession> findActiveSessions() {
        String sql = "SELECT id, target_word_id, theme_id, status, created_at FROM game_sessions WHERE status = 'IN_PROGRESS'";
        return jdbcTemplate.query(sql, gameSessionRowMapper);
    }
}