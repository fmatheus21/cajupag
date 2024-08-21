CREATE TABLE balance (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(5) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  UNIQUE KEY name_UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO balance (id, name) VALUES (1, 'FOOD');
INSERT INTO balance (id, name) VALUES (2, 'MEAL');
INSERT INTO balance (id, name) VALUES (3, 'CASH');

