drop database if exists prj321x_asm02;

create database prj321x_asm02;

use prj321x_asm02;

create table roles (
	id int not null auto_increment primary key,
    role_name varchar(255)
);

create table users (
	id int not null auto_increment primary key,
    address varchar(255),
    description varchar(255),
    email varchar(255) not null,
    full_name varchar(255) not null,
    image varchar(255),
    password varchar(128) not null,
    phone_number varchar(255),
    status int not null,
    role_id int not null,
    confirm_account int,
    foreign key(role_id) references roles (id)
);

create table cvs (
	id int not null auto_increment primary key,
    file_name varchar(255),
    user_id int,
    foreign key(user_id) references users (id)
);

create table companies (
	id int not null auto_increment primary key,
    address varchar(255),
    description text,
    email varchar(255),
    logo varchar(255),
	company_name varchar(255),
    phone_number varchar(255),
    status int not null,
    user_id int not null,
    foreign key(user_id) references users (id)
);

create table follow_companies (
	id int not null auto_increment primary key,
    company_id int not null,
    user_id int not null,
    foreign key(company_id) references companies (id),
    foreign key(user_id) references users (id)
);

create table categories (
	id int not null auto_increment primary key,
    name varchar(255) not null,
    number_choose int default 0
);

create table recruitment(
	id int not null auto_increment primary key,
    address varchar(255),
    created_at date default (curdate()),
    description varchar(255),
    experience varchar(255),
    quantity int not null,
    salary varchar(255),
    status int,
    title varchar(255),
    type varchar(255),
    view int default 0,
    deadline date,
    category_id int not null,
    company_id int not null,
    foreign key(category_id) references categories (id),
    foreign key(company_id) references companies (id)
);

create table save_job(
	id int not null auto_increment primary key,
    recruitment_id int not null,
    user_id int not null,
    foreign key(recruitment_id) references recruitments (id),
    foreign key(user_id) references users (id)
);

create table apply_posts (
	id int not null auto_increment primary key,
    created_at date default (curdate()),
    status int not null,
    text varchar(255),
    recruitment_id int not null,
    user_id int not null,
    cv_name varchar(255) not null,
    foreign key(recruitment_id) references recruitments (id),
    foreign key(user_id) references users (id)
);

insert into roles
values(1, "Công ty"), 
	  (2, "Ứng cử viên");
      
insert into categories (name)
values("NODEJS"),
	  ("PHP"),
      ("JAVA"),
      ("ASP .NET"),
      ("C#"),
      ("Ruby");
