CREATE TABLE message (
    id SERIAL PRIMARY KEY,
    fromId int,
    text VARCHAR(500),
    seen BOOLEAN,
    toId int,
    timestamp TIMESTAMP
);

CREATE TABLE person (
    id int PRIMARY KEY,
    role VARCHAR(50),
    name VARCHAR(50),
    externalId UUID,
    email VARCHAR(50)
);
)