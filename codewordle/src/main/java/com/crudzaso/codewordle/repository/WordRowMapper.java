package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.Word;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WordRowMapper implements RowMapper<Word> {
    @Override
    public Word mapRow(ResultSet rs, int rowNum) throws SQLException {
        Word word = new Word();
        word.setId(rs.getLong("id"));
        word.setWord(rs.getString("word"));
        word.setThemeId(rs.getLong("theme_id"));
        return word;
    }
}