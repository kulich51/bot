--liquibase formatted sql
--changeset kulich51: table contacts
create table contacts
(
    user_id      bigint primary key,
    phone_number varchar(20) NOT NULL,
    first_name   varchar(100),
    last_name    varchar(100)
);

--changeset danilkovich: table volunteer

create table volunteer
(
    id       serial primary key,
    username text
);
insert into volunteer
values (1, '@sammy_69'),
       (2, '@vlkulikov'),
       (3, '@vladbashlakov');

--changeset danilkovich: table adopter

CREATE TABLE adopter
(
    user_id              bigint   NOT NULL,
    pet_id               bigint   NOT NULL,
    kind                 SMALLINT NOT NULL,
    is_probation_checked BOOLEAN default false,
    date_probation       DATE,
    probation_days       INTEGER DEFAULT 30,
    extra_days           INTEGER DEFAULT 0,
    CONSTRAINT fk_user_adopter FOREIGN KEY (user_id) REFERENCES contacts (user_id)
);

CREATE TABLE photos
(
    id         serial primary key,
    data       bytea,
    media_type varchar(255)
);

CREATE TABLE reports
(
    id          serial PRIMARY KEY,
    user_id     bigint NOT NULL,
    photo_id    bigint,
    date        date   NOT NULL,
    text_report text,
    is_accepted boolean default false,
    CONSTRAINT fk_user_reports FOREIGN KEY (user_id) REFERENCES contacts (user_id),
    CONSTRAINT fk_photos foreign key (photo_id) references photos (id)

);

