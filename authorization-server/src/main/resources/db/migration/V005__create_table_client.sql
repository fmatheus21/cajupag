CREATE TABLE client (
  id binary(16) NOT NULL,
  id_person int NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  UNIQUE KEY id_person_UNIQUE (id_person),
  CONSTRAINT fk_person_client FOREIGN KEY (id_person) REFERENCES person (id) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO client (id, id_person, created_at, updated_at) VALUES (UUID_TO_BIN('230f4158-5e89-11ef-9650-581122c7752d'), 1, CURRENT_TIMESTAMP, null);

