--liquibase formatted sql
--changeset kulich51: table contacts
create table contacts
(
    id serial primary key,
    phone_number varchar(20) NOT NULL,
    first_name varchar(100),
    last_name varchar(100),
    user_id bigint NOT NULL
);

--changeset danilkovich: table volunteer

create table volunteer
(
    id       serial primary key,
    username text
);
insert into volunteer values(1,'@sammy_69'), (2,'@vlkulikov'), (3,'@vladbashlakov');
