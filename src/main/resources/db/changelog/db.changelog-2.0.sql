  --liquibase formatted sql
  --changeset yurii:1
  create table user_subscriptions (
    channel_id bigint references users(id) on delete cascade ,
    subscriber_id bigint references users(id) on delete cascade ,
     primary key (channel_id, subscriber_id)
  );

insert into user_subscriptions values (2, 16);