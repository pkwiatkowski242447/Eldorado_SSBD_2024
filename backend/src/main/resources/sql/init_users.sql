CREATE DATABASE ssbd00;
CREATE USER ssbd03admin PASSWORD 'admin';
CREATE USER ssbd03auth PASSWORD 'auth';
CREATE USER ssbd03mok PASSWORD 'mok';
GRANT ALL PRIVILEGES ON DATABASE ssbd00 TO ssbd03admin;