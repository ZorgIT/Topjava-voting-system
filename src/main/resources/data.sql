-- noinspection SqlResolveForFile
INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 3);

INSERT INTO restaurants(name)
VALUES ('Ресторан 1'),
       ('Ресторан 2'),
       ('Ресторан 3'),
       ('Ресторан 4 скоро открытие');


INSERT INTO menus_item(date_time_stamp, dish_name, price, restaurant_id)
VALUES ('2023-05-10 10:02:01', 'Борщ с салом', 150.00, 1),
       ('2023-05-10 10:02:01', 'Салат с креветкой', 130.00, 2),
       ('2023-05-10 10:02:01', 'Стейк', 140.00, 3),
       ('2023-05-13 10:02:01', 'Суп звездочки', 150.00, 1),
       ('2023-05-13 10:02:01', 'Блины со сгущенкой', 130.00, 2),
       ('2023-05-13 10:02:01', 'Стейк рибай', 140.00, 3),
       ('2023-05-21 10:02:01', 'Солянка', 150.00, 1),
       ('2023-05-21 10:02:01', 'Пончики', 130.00, 2),
       ('2023-05-21 10:02:02', 'Кофе', 50.00, 2),
       ('2023-05-21 10:02:01', '33 коровы', 140.00, 3);

INSERT INTO votes (date_time_stamp, user_id, restaurant_id)
VALUES ('2023-05-10 10:30:01', 1, 1),
       ('2023-05-10 10:30:01', 3, 2),
       ('2023-05-13 10:30:01', 1, 3),
       ('2023-05-13 10:30:01', 3, 2),
       ('2023-05-14 10:30:01', 3, 4);




