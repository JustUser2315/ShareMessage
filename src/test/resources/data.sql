insert into users(id, username, password, active)
values (1, 'username1', 'password1', true),
       (2, 'username2', 'password2', false);
SELECT SETVAL('users_id_seq', (select max(id) from users));

insert into messages(id, text, tag, user_id)
values (1, 'text1', 'tag1', 1),
       (2, 'text2', 'tag2', 1),
       (3, 'text3', 'tag3', 1);
SELECT SETVAL('messages_id_seq', (select max(id) from messages));



insert into roles(id, name)
values (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN'),
       (3, 'UNKNOWN');
SELECT SETVAL('roles_id_seq', (select max(id) from roles));


insert into users_roles(user_id, role_id)
values (1, 1),
       (1, 2);
