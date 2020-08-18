DROP TABLE IF EXISTS members;

CREATE TABLE members (
  id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
<<<<<<< HEAD
  idobata_id VARCHAR(20) NOT NULL,
  floor VARCHAR(20) DEFAULT 4,
  skip BOOLEAN DEFAULT FALSE
);
=======
  idobata_id VARCHAR(20),
  floor VARCHAR(20) DEFAULT 4,
  skip BOOLEAN DEFAULT FALSE,
  is_cleaner BOOLEAN DEFAULT FALSE
);

>>>>>>> origin/master
