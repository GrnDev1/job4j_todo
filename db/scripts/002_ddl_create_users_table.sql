CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR,
    login    VARCHAR unique,
    password VARCHAR
);