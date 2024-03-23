CREATE DATABASE ssbd03;
CREATE USER ssbd03admin PASSWORD 'admin';
CREATE USER ssbd03auth PASSWORD 'auth';
CREATE USER ssbd03mok PASSWORD 'mok';
CREATE USER ssbd03mop PASSWORD 'mop';
GRANT ALL PRIVILEGES ON SCHEMA public TO ssbd03admin;
GRANT ALL PRIVILEGES ON DATABASE ssbd03 TO ssbd03admin;
\c ssbd03;
CREATE TABLE testtable (
                           id BIGINT NOT NULL,
                           name VARCHAR(255) NOT NULL,
                           age INTEGER NOT NULL,
                           PRIMARY KEY (id)
);

CREATE TABLE testtable2 (
                            id UUID NOT NULL,
                            name VARCHAR(255) NOT NULL,
                            age INTEGER NOT NULL,
                            date DATE NOT NULL,
                            PRIMARY KEY (id)
);