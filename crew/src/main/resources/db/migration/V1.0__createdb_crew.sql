CREATE SEQUENCE hibernate_sequence START WITH 9 INCREMENT BY 1;

CREATE TABLE crew (
   crew_id VARCHAR(255) NOT NULL,
   crew_name VARCHAR(255) NOT NULL,
   crew_size INTEGER NOT NULL CHECK (crew_size >= 1),
   PRIMARY KEY (crew_id)
);

INSERT INTO crew (crew_id, crew_name, crew_size) VALUES
    ('c000', 'Crew Alpha', 12),
    ('c001', 'Crew Bravo', 150),
    ('c003', 'Crew Charlie', 3),
    ('c002', 'Crew Delta', 50),
    ('c004', 'Crew Echo', 75),
    ('c005', 'Crew Foxtrot', 250),
    ('c006', 'Crew Golf', 125),
    ('c007', 'Crew Hotel', 2),
    ('c008', 'Crew India', 30),
    ('c009', 'Crew Juliet', 500);
