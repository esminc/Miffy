TRUNCATE TABLE members RESTART IDENTITY;

INSERT INTO members (name, idobata_id, skip, is_cleaner) VALUES
('スナッフィー', 'test1', FALSE, TRUE),
('ボリス', 'test2', FALSE, FALSE),
('バーバラ', 'test3', TRUE, FALSE);
