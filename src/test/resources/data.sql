TRUNCATE TABLE members RESTART IDENTITY;

INSERT INTO members (name, idobata_id, skip, is_cleaner) VALUES
                    ('スナッフィー', 'test1', FALSE, TRUE),
                    ('ボリス', 'test2', FALSE, FALSE);
INSERT INTO members (name, idobata_id, skip, note) VALUES
                    ('バーバラ', 'test3', TRUE, '1階に移動しました');
