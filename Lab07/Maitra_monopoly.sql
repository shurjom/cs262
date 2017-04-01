--
-- This SQL script builds a monopoly database, deleting any pre-existing version.
--
-- @author shurjom
-- @version Fall, 2015
--

-- Drop previous versions of the tables if they they exist, in reverse order of foreign keys.
DROP TABLE IF EXISTS PlayerGame;
DROP TABLE IF EXISTS Property;
DROP TABLE IF EXISTS Game;
DROP TABLE IF EXISTS Player;



-- Create the schema.
CREATE TABLE Game (
	ID integer PRIMARY KEY, 
	time timestamp
	);

CREATE TABLE Player (
	ID integer PRIMARY KEY, 
	emailAddress varchar(50) NOT NULL,
	name varchar(50)
	);

CREATE TABLE Property (
	gameID integer REFERENCES Game(ID),
	playerID integer REFERENCES Player(ID),
	property varchar(50),
	hotel varchar(50),
	house varchar(50)
	);

CREATE TABLE PlayerGame (
	gameID integer REFERENCES Game(ID), 
	playerID integer REFERENCES Player(ID),
	score integer,
        cash integer,
	location varchar(50)
	);



-- Allow users to select data from the tables.
GRANT SELECT ON Game TO PUBLIC;
GRANT SELECT ON Player TO PUBLIC;
GRANT SELECT ON PlayerGame TO PUBLIC;
GRANT SELECT ON Property TO PUBLIC;

-- Add sample records.
INSERT INTO Game VALUES (1, '2006-06-27 08:00:00');
INSERT INTO Game VALUES (2, '2006-06-28 13:20:00');
INSERT INTO Game VALUES (3, '2006-06-29 18:41:00');

INSERT INTO Player(ID, emailAddress) VALUES (1, 'me@calvin.edu');
INSERT INTO Player VALUES (2, 'king@gmail.edu', 'The King');
INSERT INTO Player VALUES (3, 'dog@gmail.edu', 'Dogbreath');

INSERT INTO Property VALUES (1, 1, 'Mansion1', 'Waldorf1', 'BigHouse1');
INSERT INTO Property VALUES (1, 2, 'Mansion2', 'Waldorf2', 'BigHouse2');
INSERT INTO Property VALUES (1, 3, 'Mansion3', 'Waldorf3', 'BigHouse3');

INSERT INTO PlayerGame VALUES (1, 1, 0.00, 1000.00,'River');
INSERT INTO PlayerGame VALUES (1, 2, 0.00, 2000.00,'River2');
INSERT INTO PlayerGame VALUES (1, 3, 2350.00, 3000.00,'River3');
INSERT INTO PlayerGame VALUES (2, 1, 1000.00, 4000.00,'River4');
INSERT INTO PlayerGame VALUES (2, 2, 0.00, 5000.00,'River5');
INSERT INTO PlayerGame VALUES (2, 3, 500.00, 6000.00,'River6');
INSERT INTO PlayerGame VALUES (3, 2, 0.00, 7000.00,'River7');
INSERT INTO PlayerGame VALUES (3, 2, 0.00, 8000.00,'River8');
INSERT INTO PlayerGame VALUES (3, 3, 5500.00, 9000.00,'River9');