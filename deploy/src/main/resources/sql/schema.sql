-- 连接信息
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
    password        TEXT    not null,
    create_time     REAL    not null,
    modify_time     REAL    not null
);

create unique index main.uni_connection_id
    on main.connection_definition (connection_id);

-- 流程信息
create table main.process
(
    id           INTEGER not null
        constraint pk_id
            primary key autoincrement,
    process_id   TEXT    not null,
    process_name TEXT    not null,
    create_time  REAL    not null,
    modify_time  REAL    not null
);

create unique index main.uni_process_id
    on main.process (process_id);

-- 流程节点
create table main.process_node
(
    id          INTEGER not null
        constraint pk_id
            primary key autoincrement,
    node_id     TEXT    not null,
    node_type   TEXT    not null,
    parent_id   TEXT,
    process_id  TEXT    not null,
    create_time REAL    not null,
    modify_time REAL    not null
);

create index main.idx_process_id
    on main.process_node (process_id);

create unique index main.uni_node_id
    on main.process_node (node_id);

-- 流程会话表
create table main.process_session
(
    id          INTEGER not null
        constraint pk_id
            primary key autoincrement,
    session_id  TEXT    not null,
    process_id  TEXT    not null,
    create_time REAL    not null,
    modify_time REAL    not null
);

create index main.idx_process_id
    on main.process_session (process_id);


