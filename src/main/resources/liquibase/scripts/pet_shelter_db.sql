--liquibase formatted sql
--changeset kulich51: table contacts
create table contacts(
    user_id bigint primary key,
    chat_id bigint,
    username text,
    first_name text
);

-- create table user_appeal(
--     id serial primary key,
--     user_id bigint,
--     question_solved bool,
--     CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES contacts(user_id)
-- );