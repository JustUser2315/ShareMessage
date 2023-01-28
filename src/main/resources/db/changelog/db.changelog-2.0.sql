  --liquibase formatted sql

  --changeset yurii:1
create table roles(
    id bigserial primary key not null ,
    name varchar(64) not null check ( name like 'ROLE_%')
);
insert into roles(name) values ('ROLE_USER'), ('ROLE_ADMIN');

  --changeset yurii:2
create table users_roles(
    user_id bigserial references users(id) on delete cascade on update cascade ,
    role_id bigserial references roles(id) on delete cascade on update cascade
);

