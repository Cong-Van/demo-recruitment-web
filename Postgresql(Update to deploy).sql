CREATE TABLE roles (
    id serial PRIMARY KEY,
    role_name varchar(255)
);

CREATE TABLE users (
    id serial PRIMARY KEY,
    address varchar(255),
    description varchar(255),
    email varchar(255) NOT NULL,
    full_name varchar(255) NOT NULL,
    image varchar(255),
    password varchar(128) NOT NULL,
    phone_number varchar(255),
    status int NOT NULL,
    role_id int NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE cvs (
    id serial PRIMARY KEY,
    file_name varchar(255),
    user_id int,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE companies (
    id serial PRIMARY KEY,
    address varchar(255),
    description text,
    email varchar(255),
    logo varchar(255),
    company_name varchar(255),
    phone_number varchar(255),
    status int NOT NULL,
    user_id int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE follow_companies (
    id serial PRIMARY KEY,
    company_id int NOT NULL,
    user_id int NOT NULL,
    FOREIGN KEY (company_id) REFERENCES companies (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE categories (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    number_choose int DEFAULT 0
);

CREATE TABLE recruitments (
    id serial PRIMARY KEY,
    address varchar(255),
    created_at date DEFAULT current_date,
    description varchar(255),
    experience varchar(255),
    quantity int NOT NULL,
    salary varchar(255),
    status int,
    title varchar(255),
    type varchar(255),
    view int DEFAULT 0,
    deadline date,
    category_id int NOT NULL,
    company_id int NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE TABLE save_jobs (
    id serial PRIMARY KEY,
    recruitment_id int NOT NULL,
    user_id int NOT NULL,
    FOREIGN KEY (recruitment_id) REFERENCES recruitments (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE apply_posts (
    id serial PRIMARY KEY,
    created_at date DEFAULT current_date,
    status int NOT NULL,
    text varchar(255),
    recruitment_id int NOT NULL,
    user_id int NOT NULL,
    cv_name varchar(255) NOT NULL,
    FOREIGN KEY (recruitment_id) REFERENCES recruitments (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

INSERT INTO roles (id, role_name)
VALUES (1, 'Công ty'),
       (2, 'Ứng cử viên');

INSERT INTO categories (name)
VALUES ('NODEJS'),
       ('PHP'),
       ('JAVA'),
       ('ASP .NET'),
       ('C#'),
       ('Ruby');