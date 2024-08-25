CREATE TABLE transaction (
  id binary(16) NOT NULL,
  id_account binary(16) NOT NULL,
  id_balance int NOT NULL,
  id_establishment binary(16) NOT NULL,
  amount decimal(8,2) NOT NULL,
  status varchar(70) NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  KEY fk_establishment_transaction_idx (id_establishment),
  KEY fk_account_transaction_idx (id_account),
  KEY fk_balance_transaction_idx (id_balance),
  CONSTRAINT fk_account_transaction FOREIGN KEY (id_account) REFERENCES account (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT fk_balance_transaction FOREIGN KEY (id_balance) REFERENCES balance (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT fk_establishment_transaction FOREIGN KEY (id_establishment) REFERENCES establishment (id) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;