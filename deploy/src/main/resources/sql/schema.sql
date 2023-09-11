create table main.connection_definition
(
    id              INTEGER not null
        constraint pk_id
            primary key autoincrement,
    connection_id   TEXT    not null,
    connection_name TEXT    not null,
    url             TEXT    not null,
    schema_name     TEXT    not null,
    username        TEXT    not null,
    password        TEXT    not null
);

create unique index main.uniq_connection_id
    on main.connection_definition (connection_id);

