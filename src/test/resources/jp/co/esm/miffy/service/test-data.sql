TRUNCATE TABLE members RESTART IDENTITY;

INSERT INTO members (name, idobata_id, skip) VALUES
('スナッフィー', 'test1', FALSE),
('ボリス', 'test2', FALSE),
('バーバラ', 'test3', TRUE);
