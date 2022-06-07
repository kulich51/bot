--liquibase formatted sql
--changeset kulich51: table contacts
create table contacts
(
    user_id    bigint primary key,
    chat_id    bigint,
    username   text,
    first_name text
    --user_message text
);

--changeset danilkovich: table volunteer

create table volunteer
(
    id       serial primary key,
    username text
);
insert into volunteer values(1,'@sammy_69'), (2,'@vlkulikov'), (3,'@vladbashlakov');
