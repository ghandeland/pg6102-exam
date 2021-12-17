CREATE SEQUENCE hibernate_sequence START WITH 8 INCREMENT BY 1;

CREATE TABLE port (
   port_id BIGINT NOT NULL,
   name VARCHAR(255) NOT NULL,
   city VARCHAR(255) NOT NULL,
   country VARCHAR(255) NOT NULL,
   PRIMARY KEY (port_id)
);

INSERT INTO port (port_id, name, city, country) VALUES
    (0, 'Port of Helsinki', 'Helsinki', 'Finland'),
    (1, 'Port of Shanghai', 'Shanghai', 'China'),
    (2, 'Port of Tallinn', 'Tallinn', 'Estonia'),
    (3, 'Port of Stockholm', 'Stockholm', 'Sweden'),
    (4, 'Port of Rotterdam', 'Rotterdam', 'Netherlands'),
    (5, 'Port of Los Angeles', 'Los Angeles', 'United States'),
    (6, 'Port of Hamburg', 'Hamburg', 'Germany'),
    (7, 'Port of Saigon', 'Saigon', 'Vietnam');
