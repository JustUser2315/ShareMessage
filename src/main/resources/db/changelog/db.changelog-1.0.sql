  --liquibase formatted sql
  --changeset yurii:1
  create table users(
                        id bigserial primary key not null ,
                        username varchar(256) not null unique ,
                        password varchar(256) not null ,
                        active boolean default true
  );
insert into users( username, password) values ('username1', 'password1');

  --changeset yurii:2
create table messages(
    id serial primary key ,
    text varchar(2000) not null ,
    tag varchar(25),
    user_id bigserial references users(id) on delete cascade
);
insert into messages (text, tag, user_id) values ('text1', 'tag1', 1);