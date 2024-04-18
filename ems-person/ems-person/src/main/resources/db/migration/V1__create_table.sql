CREATE TABLE person
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    email       VARCHAR(50) NOT NULL UNIQUE,
    role        VARCHAR(50) NOT NULL,
    external_id UUID        NOT NULL UNIQUE
);