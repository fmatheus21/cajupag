CREATE TABLE account_balance_join (
  id_account binary(16) NOT NULL,
  id_balance int NOT NULL,
  current_balance decimal(8,2) NOT NULL,
  total_balance decimal(8,2) NOT NULL,
  PRIMARY KEY (id_account, id_balance),
  KEY fk_balance_idx (id_balance),
  CONSTRAINT fk_account FOREIGN KEY (id_account) REFERENCES account (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT fk_balance FOREIGN KEY (id_balance) REFERENCES balance (id) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO account_balance_join (id_account, id_balance, current_balance, total_balance) VALUES (UUID_TO_BIN('0015dc51-05d5-11ee-900d-7085c2be6d69'), 1, 659.57, 1000.0);
INSERT INTO account_balance_join (id_account, id_balance, current_balance, total_balance) VALUES (UUID_TO_BIN('0015dc51-05d5-11ee-900d-7085c2be6d69'), 2, 500.0, 500.0);
INSERT INTO account_balance_join (id_account, id_balance, current_balance, total_balance) VALUES (UUID_TO_BIN('0015dc51-05d5-11ee-900d-7085c2be6d69'), 3, 200.0, 200.0);

