--liquibase formatted sql

create schema if not exists link_tracking_db;

create table if not exists link_tracking_db.chat
(
    id         bigint not null,
    created_at timestamp with time zone default current_timestamp,

    primary key (id)
);

create table if not exists link_tracking_db.link
(
    id         bigint        not null,
    url        varchar(1024) not null,
    updated_at timestamp with time zone default current_timestamp,

    primary key (id)
);

create table if not exists link_tracking_db.chat_link_relations
(
    chat_id bigint not null references link_tracking_db.chat (id) on delete cascade,
    link_id bigint not null references link_tracking_db.link (id) on delete cascade,

    primary key (chat_id, link_id)
);
