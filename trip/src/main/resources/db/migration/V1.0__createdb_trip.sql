CREATE SEQUENCE hibernate_sequence START WITH 6 INCREMENT BY 1;

CREATE TABLE trip (
   trip_id BIGINT NOT NULL,
   origin_port_id BIGINT NOT NULL,
   destination_port_id BIGINT NOT NULL,
   boat_id BIGINT NOT NULL,
   crew_id VARCHAR(255) NOT NULL,
   PRIMARY KEY (trip_id)
);

INSERT INTO trip (trip_id, origin_port_id, destination_port_id, boat_id, crew_id)  VALUES
    (0, 2, 4, 2, 'c000'),
    (1, 2, 7, 0, 'c005'),
    (2, 5, 1, 5, 'c008'),
    (3, 2, 3, 6, 'c002'),
    (4, 5, 6, 11, 'c007'),
    (5, 0, 1, 4, 'c003');
