CREATE TABLE account (
  id binary(16) NOT NULL,
  id_client binary(16) NOT NULL,
  total_amount decimal(8,2) NOT NULL,
  card_number varchar(20) NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  UNIQUE KEY card_number_UNIQUE (card_number),
  KEY fk_client_account_idx (id_client),
  CONSTRAINT fk_client_account FOREIGN KEY (id_client) REFERENCES client (id) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO account(id, id_client, total_amount, card_number, created_at)
VALUES(
UUID_TO_BIN('0015dc51-05d5-11ee-900d-7085c2be6d69'),
UUID_TO_BIN('230f4158-5e89-11ef-9650-581122c7752d'),
4590.40,
'5588623798347322',
CURRENT_TIMESTAMP
);

