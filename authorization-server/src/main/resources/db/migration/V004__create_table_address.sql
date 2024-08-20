CREATE TABLE address (
  id INT NOT NULL AUTO_INCREMENT,
  id_person INT NOT NULL,
  place varchar(70) NOT NULL,
  number varchar(10) NOT NULL,
  complement varchar(50) DEFAULT NULL,
  district varchar(70) NOT NULL,
  city varchar(70) NOT NULL,
  state varchar(2) NOT NULL,
  zip_code varchar(15) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  UNIQUE KEY id_person_UNIQUE (id_person),
  CONSTRAINT fk_person_address FOREIGN KEY (id_person) REFERENCES person (id) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO address (id, id_person, place, number, complement, district, city, state, zip_code)
VALUES (1, 1, 'AVENIDA DAS AMÉRICAS', '12000', 'APT 401', 'BARRA DA TIJUCA', 'RIO DE JANEIRO', 'RJ', '22793082');