CREATE SEQUENCE hibernate_sequence START WITH 9 INCREMENT BY 1;

CREATE TABLE boat (
   boat_id BIGINT NOT NULL,
   name VARCHAR(255) NOT NULL,
   min_crew_size INTEGER NOT NULL CHECK ( min_crew_size >= 1 ),
   max_crew_size INTEGER NOT NULL CHECK ( max_crew_size >= 1 ),
   PRIMARY KEY (boat_id)
);

INSERT INTO boat (boat_id, name, min_crew_size, max_crew_size) VALUES
    (1, 'Evergreen', 150, 500),
    (2, 'The Ferry', 20, 50),
    (3, 'Speedboat', 1, 4),
    (4, 'Sailboat', 4, 12),
    (5, 'Blue dream', 10, 100),
    (6, 'Shipping boat', 10, 200),
    (7, 'Container boat', 15, 250),
    (8, 'Oil tanker', 8, 150),
    (9, 'Large sailboat', 4, 40),
    (10, 'Submarine', 50, 250),
    (11, 'Row boat', 1, 2),
    (12, 'Fishing boat', 3, 20);
