CREATE TABLE contact (
  id INT NOT NULL AUTO_INCREMENT,
  id_person INT NOT NULL,
  phone varchar(20) NOT NULL,
  email varchar(200) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  UNIQUE KEY id_person_UNIQUE (id_person),
  UNIQUE KEY phone_UNIQUE (phone),
  UNIQUE KEY email_UNIQUE (email),
  CONSTRAINT fk_person_contact FOREIGN KEY (id_person) REFERENCES person (id) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO contact (id, id_person, phone, email) VALUES (1, 1, '21986731552', 'FERNANDO.MATHEUSS@HOTMAIL.COM');

