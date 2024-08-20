CREATE TABLE person (
  id INT NOT NULL AUTO_INCREMENT,
  id_person_type INT NOT NULL,
  name varchar(70) NOT NULL,
  document varchar(20) NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  UNIQUE KEY document_UNIQUE (document),
  KEY fk_person_type_idx (id_person_type),
  CONSTRAINT fk_person_type FOREIGN KEY (id_person_type) REFERENCES person_type (id) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO person (id, id_person_type, name, document, created_at) VALUES (1, 1, 'ROGERIO CARDOSO DE CASTRO', '67780886050', CURRENT_TIMESTAMP);
INSERT INTO person (id, id_person_type, name, document, created_at) VALUES (2, 2, 'MS RESTAURANTE ME', '65677381000193', CURRENT_TIMESTAMP);
