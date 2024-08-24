CREATE TABLE establishment (
  id binary(16) NOT NULL,
  id_person int NOT NULL,
  merchant varchar(70),
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  UNIQUE KEY id_person_UNIQUE (id_person),
  UNIQUE KEY merchant_UNIQUE (merchant),
  CONSTRAINT fk_person_establishment FOREIGN KEY (id_person) REFERENCES person (id) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO establishment (id, id_person, merchant, created_at) VALUES
(UUID_TO_BIN('e003f62a-5e8c-11ef-9650-581122c7752d'), 2, 'MS RESTAURANTE - SAO PAULO BR', CURRENT_TIMESTAMP);
