package com.crudzaso.codewordle.repository;

import com.crudzaso.codewordle.model.Theme;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ThemeRowMapper implements RowMapper<Theme> {
    @Override
    public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
        Theme theme = new Theme();
        theme.setId(rs.getLong("id"));
        theme.setName(rs.getString("name"));
        theme.setDescription(rs.getString("description"));
        return theme;
    }
}