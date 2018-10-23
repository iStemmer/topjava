DELETE FROM user_roles;
DELETE FROM users;
delete from meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

insert into meals( description, datetime, calories, user_id) values
  ('Завтрак', '2015-05-30 10:30:00', 500, 100000 ),
  ('Обед', '2015-05-30 13:00:00', 1000,100000 ),
  ('Ужин', '2015-05-30 20:00:00', 500, 100000 ),
  ('Завтрак', '2015-05-30 10:30:00', 500, 100001 ),
  ('Обед', '2015-05-30 13:00:00', 1000,100001 ),
  ('Ужин', '2015-05-30 20:00:00', 500, 100001 );

