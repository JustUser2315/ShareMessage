  --liquibase formatted sql
  --changeset yurii:1
  create table users_likes (
    user_id bigint references users(id) on delete cascade ,
    message_id int references messages(id) on delete cascade ,
     primary key (user_id, message_id)
  );

insert into users_likes values (2, 16);