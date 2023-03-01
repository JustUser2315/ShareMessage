insert into users(id, username, password, active, activation_code, email)
values (1, 'username1', 'password1', false, 'activation-code-example', 'test1@mail.com'),
       (2, 'username2', 'password2', true, null, 'test2@mail.com');
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
