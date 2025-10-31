package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.GameSession;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class GameSessionRowMapper implements RowMapper<GameSession> {
    @Override
    public GameSession mapRow(ResultSet rs, int rowNum) throws SQLException {
        GameSession gameSession = new GameSession();
        gameSession.setId(rs.getLong("id"));
        gameSession.setTargetWordId(rs.getLong("target_word_id"));
        gameSession.setThemeId(rs.getLong("theme_id"));

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            gameSession.setStatus(GameSession.GameStatus.valueOf(statusStr));
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            gameSession.setCreatedAt(createdAt.toLocalDateTime());
        }

        return gameSession;
    }
}