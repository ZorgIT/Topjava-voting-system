INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO restaurants(name)
VALUES ('Ресторан 1'),
       ('Ресторан 2'),
       ('Ресторан 3');

INSERT INTO menus(date, dish, price, restaurant_id)
VALUES ('2023-05-10','Борщ с салом',150.00,1),
       ('2023-05-10','Салат с креветкой',150.00,1),
       ('2023-05-10','Стейк',150.00,1);

INSERT INTO votes(date, user_id, menu_id)
VALUES ('2023-05-10',1,1),
       ('2023-05-10',2,2),
       ('2023-05-10',3,3);



