package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.Attempt;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AttemptRowMapper implements RowMapper<Attempt> {
    @Override
    public Attempt mapRow(ResultSet rs, int rowNum) throws SQLException {
        Attempt attempt = new Attempt();
        attempt.setId(rs.getLong("id"));
        attempt.setGameSessionId(rs.getLong("game_session_id"));
        attempt.setGuessWord(rs.getString("guess_word"));
        attempt.setAttemptNumber(rs.getInt("attempt_number"));
        attempt.setFeedback(rs.getString("feedback"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            attempt.setCreatedAt(createdAt.toLocalDateTime());
        }

        return attempt;
    }
}