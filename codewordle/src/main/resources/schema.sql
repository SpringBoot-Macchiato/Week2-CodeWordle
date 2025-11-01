-- Database Schema for CodeWordle

-- Themes table
CREATE TABLE IF NOT EXISTS themes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200)
);

-- Words table
CREATE TABLE IF NOT EXISTS words (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    word VARCHAR(50) NOT NULL,
    theme_id BIGINT NOT NULL,
    FOREIGN KEY (theme_id) REFERENCES themes(id),
    UNIQUE(word, theme_id)
);

-- Game sessions table
CREATE TABLE IF NOT EXISTS game_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    target_word_id BIGINT NOT NULL,
    theme_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'IN_PROGRESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (target_word_id) REFERENCES words(id),
    FOREIGN KEY (theme_id) REFERENCES themes(id)
);

-- Attempts table
CREATE TABLE IF NOT EXISTS attempts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_session_id BIGINT NOT NULL,
    guess_word VARCHAR(50) NOT NULL,
    attempt_number INT NOT NULL,
    feedback VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (game_session_id) REFERENCES game_sessions(id),
    UNIQUE(game_session_id, attempt_number)
);