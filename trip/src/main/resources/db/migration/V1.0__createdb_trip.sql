CREATE SEQUENCE hibernate_sequence START WITH 9 INCREMENT BY 1;

CREATE TABLE trip (
   trip_id BIGINT NOT NULL,
   origin_port_id BIGINT NOT NULL,
   destination_port_id BIGINT NOT NULL,
   boat_id BIGINT NOT NULL,
   crew_id VARCHAR(255) NOT NULL,
   PRIMARY KEY (trip_id)
);

/* INSERT INTO trip (port_id, name, city, country) VALUES */
