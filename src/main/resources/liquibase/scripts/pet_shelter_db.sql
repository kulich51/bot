--liquibase formatted sql
--changeset kulich51: table contacts
create table contacts
(
    user_id      bigint primary key,
    phone_number varchar(20) NOT NULL,
    first_name   varchar(100),
    last_name    varchar(100),
    chat_id      bigint
);

--changeset danilkovich_1: table volunteer

create table volunteer
(
    id       serial primary key,
    username text
);

insert into volunteer
values (1, '@sammy_69'),
       (2, '@vlkulikov'),
       (3, '@vladbashlakov');

--changeset danilkovich_2: table adopter

CREATE TABLE adopter
(
    id serial PRIMARY KEY,
    user_id                     bigint   NOT NULL,
    pet_id                      bigint   NOT NULL,
    is_probation_checked        BOOLEAN default false,
    start_date_probation        DATE,
    finish_date_probation           DATE,
    CONSTRAINT fk_user_adopter FOREIGN KEY (user_id) REFERENCES contacts (user_id)
);

--changeset kulich52: table pets
CREATE TABLE pets
(
    id serial primary key,
    name varchar(30) UNIQUE,
    kind varchar(3) NOT NULL
);

ALTER TABLE pets ADD CONSTRAINT allowed_pet_kinds CHECK ( kind = ANY ('{CAT, DOG}' :: text[]));

CREATE TABLE reports
(
    id          serial PRIMARY KEY,
    user_id     bigint NOT NULL,
    pet_id      bigint NOT NULL,
    file_path     text,
    date        date   NOT NULL,
    text_report text,
    is_accepted boolean default true,
    CONSTRAINT fk_user_reports FOREIGN KEY (user_id) REFERENCES contacts (user_id),
    CONSTRAINT fk_pet_reports FOREIGN KEY (pet_id) REFERENCES pets (id)

);