CREATE TABLE person_type (
  id INT NOT NULL AUTO_INCREMENT,
  name varchar(15) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  UNIQUE KEY name_UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO person_type (id, name) VALUES (1, 'PESSOA FISICA');
INSERT INTO person_type (id, name) VALUES (2, 'PESSOA JURIDICA');