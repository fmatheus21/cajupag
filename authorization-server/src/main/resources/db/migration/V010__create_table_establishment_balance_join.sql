CREATE TABLE establishment_balance_join (
  id_establishmen binary(16) NOT NULL,
  id_balance int NOT NULL,
  PRIMARY KEY (id_establishmen,id_balance),
  KEY fk__balance_idx (id_balance),
  CONSTRAINT fk__balance FOREIGN KEY (id_balance) REFERENCES balance (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT fk_establishment FOREIGN KEY (id_establishmen) REFERENCES establishment (id) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO establishment_balance_join (id_establishmen, id_balance) VALUES (UUID_TO_BIN('e003f62a-5e8c-11ef-9650-581122c7752d'), 1);
INSERT INTO establishment_balance_join (id_establishmen, id_balance) VALUES (UUID_TO_BIN('e003f62a-5e8c-11ef-9650-581122c7752d'), 3);
