DROP TABLE IF EXISTS members;

CREATE TABLE members (
  id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  idobata_id VARCHAR(20) NOT NULL,
  skip BOOLEAN DEFAULT FALSE,
  note VARCHAR(255),
  is_cleaner BOOLEAN DEFAULT FALSE
);

