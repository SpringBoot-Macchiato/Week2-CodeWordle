-- Initial Data for CodeWordle

-- Insert themes
INSERT INTO themes (name, description) VALUES
('JAVA', 'Java programming language terms'),
('SPRING', 'Spring Framework concepts'),
('DEVOPS', 'DevOps tools and practices'),
('DATABASE', 'Database and SQL concepts');

-- Insert Java words
INSERT INTO words (word, theme_id) VALUES
('CLASS', 1),
('OBJECT', 1),
('METHOD', 1),
('STRING', 1),
('ARRAY', 1),
('LOOP', 1),
('THREAD', 1),
('STREAM', 1),
('LAMBDA', 1),
('EXCEPTION', 1);

-- Insert Spring words
INSERT INTO words (word, theme_id) VALUES
('BEAN', 2),
('CONTEXT', 2),
('BOOT', 2),
('MVC', 2),
('REST', 2),
('AOP', 2),
('IOC', 2),
('JPA', 2),
('JDBC', 2),
('SECURITY', 2);

-- Insert DevOps words
INSERT INTO words (word, theme_id) VALUES
('DOCKER', 3),
('KUBE', 3),
('GIT', 3),
('JENKINS', 3),
('CI', 3),
('CD', 3),
('MONITOR', 3),
('SCALE', 3),
('DEPLOY', 3),
('CLOUD', 3);

-- Insert Database words
INSERT INTO words (word, theme_id) VALUES
('QUERY', 4),
('TABLE', 4),
('INDEX', 4),
('JOIN', 4),
('KEY', 4),
('SCHEMA', 4),
('TRANSACTION', 4),
('NORMALIZATION', 4),
('VIEW', 4),
('TRIGGER', 4);