package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.Attempt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class JdbcAttemptRepository implements AttemptRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Attempt> attemptRowMapper;

    public JdbcAttemptRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.attemptRowMapper = new AttemptRowMapper();
    }

    @Override
    public Attempt save(Attempt attempt) {
        String sql = "INSERT INTO attempts (game_session_id, guess_word, attempt_number, feedback, created_at) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, attempt.getGameSessionId());
            ps.setString(2, attempt.getGuessWord());
            ps.setInt(3, attempt.getAttemptNumber());
            ps.setString(4, attempt.getFeedback());
            ps.setTimestamp(5, Timestamp.valueOf(attempt.getCreatedAt()));
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKeyAs(Long.class);
        attempt.setId(generatedId);
        return attempt;
    }

    @Override
    public List<Attempt> findByGameSessionId(Long gameSessionId) {
        String sql = "SELECT id, game_session_id, guess_word, attempt_number, feedback, created_at FROM attempts WHERE game_session_id = ? ORDER BY attempt_number";
        return jdbcTemplate.query(sql, attemptRowMapper, gameSessionId);
    }

    @Override
    public int countByGameSessionId(Long gameSessionId) {
        String sql = "SELECT COUNT(*) FROM attempts WHERE game_session_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, gameSessionId);
        return count != null ? count : 0;
    }
}