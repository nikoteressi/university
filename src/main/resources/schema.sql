drop table if exists groups, schedule, student, audience, lecture;

create table if not exists groups
(
    id          serial primary key,
    number      int not null
);

create table if not exists schedule
(
    id       serial primary key,
    date     varchar(10) not null,
    group_id bigint      not null,
    constraint fk_schedule_group_id foreign key (group_id) references groups (id)
);

create table if not exists student
(
    id         serial primary key,
    first_name VARCHAR(25) not null,
    last_name  VARCHAR(50) not null,
    group_id   bigint,
    constraint fk_student_group_id foreign key (group_id) references groups (id)
);

create table if not exists audience
(
    id     serial primary key,
    number bigint not null
);

create table if not exists lecture
(
    id          serial primary key,
    name        varchar(255) not null,
    date        varchar(10)  not null,
    group_id    bigint       not null,
    audience_id bigint       not null,
    constraint fk_lecture_group_id foreign key (group_id) references groups (id),
    constraint fk_lecture_audience_id foreign key (audience_id) references audience (id)
);