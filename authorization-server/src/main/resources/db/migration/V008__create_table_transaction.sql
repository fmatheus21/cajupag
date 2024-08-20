CREATE TABLE transaction (
  id binary(16) NOT NULL,
  id_account binary(16) NOT NULL,
  id_establishment binary(16) NOT NULL,
  amount decimal(8,2) NOT NULL,
  mcc varchar(4) NOT NULL,
  merchant varchar(150) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  KEY fk_establishment_transaction_idx (id_establishment),
  KEY fk_account_transaction_idx (id_account),
  CONSTRAINT fk_account_transaction FOREIGN KEY (id_account) REFERENCES account (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT fk_establishment_transaction FOREIGN KEY (id_establishment) REFERENCES establishment (id) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO 'transaction' (id, id_account, id_establishment, amount, mcc, merchant)
VALUES(
UUID_TO_BIN('fce51576-5e8e-11ef-9650-581122c7752d'),
UUID_TO_BIN('0015dc51-05d5-11ee-900d-7085c2be6d69'),
UUID_TO_BIN('e003f62a-5e8c-11ef-9650-581122c7752d'),
340.43,
<{mcc: }>,
<{merchant: }>
);
