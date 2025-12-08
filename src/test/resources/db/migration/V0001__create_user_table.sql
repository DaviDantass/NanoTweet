create table user
(
    id         bigint auto_increment,
    username   varchar(10) not null,
    created_at timestamp   not null,
    primary key (id)
);

create unique index uk_username on user (username);
