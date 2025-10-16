CREATE TABLE roles (
id serial PRIMARY KEY,
name varchar(100) UNIQUE NOT NULL,
description text
);

CREATE TABLE users (
id serial PRIMARY KEY,
username varchar(100) UNIQUE NOT NULL,
password varchar(255) NOT NULL,
email varchar(255)
);

CREATE TABLE projects (
id serial PRIMARY KEY,
name varchar(255) NOT NULL,
description text,
owner_id bigint REFERENCES users(id)
);

CREATE TABLE project_assignments (
id serial PRIMARY KEY,
project_id bigint REFERENCES projects(id),
user_id bigint REFERENCES users(id),
role_id bigint REFERENCES roles(id),
UNIQUE(project_id, user_id)
);