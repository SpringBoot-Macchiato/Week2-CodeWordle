-- Initial Data for CodeWordle

-- Insert themes
INSERT INTO themes (name, description) VALUES
('JAVA', 'Java programming language terms'),
('SPRING', 'Spring Framework concepts'),
('DEVOPS', 'DevOps tools and practices'),
('DATABASE', 'Database and SQL concepts');

-- Insert Java words (5 letters only)
INSERT INTO words (word, theme_id) VALUES
('CLASS', 1),
('ARRAY', 1),
('FIELD', 1),
('BYTES', 1),
('SHORT', 1),
('FLOAT', 1),
('FINAL', 1),
('SUPER', 1),
('VALUE', 1),
('STACK', 1);

-- Insert Spring words (5 letters only)
INSERT INTO words (word, theme_id) VALUES
('BEANS', 2),
('BOOTS', 2),
('MODEL', 2),
('FRAME', 2),
('SCOPE', 2),
('INJEC', 2),
('QUERY', 2),
('DRIVE', 2),
('TESTS', 2),
('CACHE', 2);

-- Insert DevOps words (5 letters only)
INSERT INTO words (word, theme_id) VALUES
('MERGE', 3),
('PIPEL', 3),
('DEPLY', 3),
('SCALE', 3),
('CLOUD', 3),
('AGILE', 3),
('BUILD', 3),
('DEPOT', 3),
('KUBES', 3),
('PIPES', 3);

-- Insert Database words (5 letters only)
INSERT INTO words (word, theme_id) VALUES
('QUERY', 4),
('TABLE', 4),
('INDEX', 4),
('JOINS', 4),
('FOREI', 4),
('VIEWS', 4),
('SCHEM', 4),
('DATAS', 4),
('TUPLE', 4),
('COLUM', 4);