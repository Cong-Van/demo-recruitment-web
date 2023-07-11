drop database if exists prj321x_asm02;

create database prj321x_asm02;

use prj321x_asm02;

create table role(
	id int(11) not null auto_increment primary key,
    role_name varchar(255)
);

create table user(
	id int(11) not null auto_increment primary key,
    address varchar(255),
    description varchar(255),
    email varchar(255) not null,
    full_name varchar(255) not null,
    image varchar(255),
    password varchar(128) not null,
    phone_number varchar(255),
    status int(11) not null,
    role_id int(11) not null,
    foreign key(role_id) references role(id)
);

create table cv(
	id int(11) not null auto_increment primary key,
    file_name varchar(255),
    user_id int(11),
    foreign key(user_id) references user(id)
);

create table company(
	id int(11) not null auto_increment primary key,
    address varchar(255),
    description text,
    email varchar(255),
    logo varchar(255),
	company_name varchar(255),
    phone_number varchar(255),
    status int(11) not null,
    user_id int(11) not null,
    foreign key(user_id) references user(id)
);

create table follow_company(
	id int(11) not null auto_increment primary key,
    company_id int(11) not null,
    user_id int(11) not null,
    foreign key(company_id) references company(id),
    foreign key(user_id) references user(id)
);

create table category(
	id int(11) not null auto_increment primary key,
    name varchar(255) not null,
    number_choose int(11) default 0
);

create table recruitment(
	id int(11) not null auto_increment primary key,
    address varchar(255),
    created_at date default (curdate()),
    description varchar(255),
    experience varchar(255),
    quantity int(11) not null,
    salary varchar(255),
    status int(11),
    title varchar(255),
    type varchar(255),
    view int(11) default 0,
    deadline date,
    category_id int(11) not null,
    company_id int(11) not null,
    foreign key(category_id) references category(id),
    foreign key(company_id) references company(id)
);

create table save_job(
	id int(11) not null auto_increment primary key,
    recruitment_id int(11) not null,
    user_id int(11) not null,
    foreign key(recruitment_id) references recruitment(id),
    foreign key(user_id) references user(id)
);

create table apply_post(
	id int(11) not null auto_increment primary key,
    created_at date default (curdate()),
    status int(11) not null,
    text varchar(255),
    recruitment_id int(11) not null,
    user_id int(11) not null,
    cv_name varchar(255) not null,
    foreign key(recruitment_id) references recruitment(id),
    foreign key(user_id) references user(id)
);

insert into role 
values(1, "Công ty"), 
	  (2, "Ứng cử viên");
      
insert into category(name)
values("NODEJS"),
	  ("PHP"),
      ("JAVA"),
      ("ASP .NET"),
      ("C#"),
      ("Ruby");
