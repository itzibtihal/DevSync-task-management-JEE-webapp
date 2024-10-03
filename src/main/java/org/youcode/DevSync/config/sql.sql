CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),  -- Using PostgresSQL's function to generate UUIDs
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       firstName VARCHAR(255),
                       lastName VARCHAR(255),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       role VARCHAR(255) NOT NULL,  -- Assuming Role is stored as a String
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP
);
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
select * from users




S
