  --liquibase formatted sql
  --changeset yurii:1
  create table users(
                        id bigserial primary key not null ,
                        username varchar(256) not null unique ,
                        email varchar(2056) unique not null,
                        password varchar(256) not null ,
                        active boolean default false,
                        activation_code varchar(2056),
                        avatar varchar

  );

insert into users(username, password, email) values ('username1', 'password1', 'test@mail.com');

  --changeset yurii:2
create table messages(
    id serial primary key ,
    text varchar(2000) not null ,
    tag varchar(25),
    user_id bigserial references users(id) on delete cascade
);
insert into messages (text, tag, user_id) values ('text1', 'tag1', 1);

  --changeset yurii:3
  create table roles(
                        id bigserial primary key not null ,
                        name varchar(64) not null check ( name like 'ROLE_%')
  );
  insert into roles(name) values ('ROLE_USER'), ('ROLE_ADMIN');

  --changeset yurii:4
  create table users_roles(
                              user_id bigserial references users(id) on delete cascade on update cascade ,
                              role_id bigserial references roles(id) on delete cascade on update cascade
  );