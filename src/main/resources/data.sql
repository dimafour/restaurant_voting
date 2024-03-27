INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@ya.ru', 'user'),
       ('User2', 'user2@ya.ru', 'user2'),
       ('User3', 'user3@ya.ru', 'user3'),
       ('Admin', 'admin@ya.ru', 'admin');


INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('USER', 2),
       ('USER', 3),
       ('ADMIN', 4),
       ('USER', 4);

INSERT INTO RESTAURANT (NAME)
VALUES ('Tasty and that-s it'),
       ('KFC'),
       ('Dolphinwolf');

INSERT INTO MEAL (MEAL_DATE, PRICE, RESTAURANT_ID, NAME)
VALUES (now(), 50000, 1, 'Big Hit'),
       (now(), 30000, 1, 'Cheeseburger'),
       (now(), 70000, 1, 'Big Tasty'),
       (now(), 40000, 2, 'Hot Wings'),
       (now(), 20000, 2, 'Twister'),
       (now(), 60000, 3, 'Lager Beer'),
       (now(), 60000, 3, 'Indian Pale Ale');

INSERT INTO VOTE (USER_ID, RESTAURANT_ID, VOTE_DATE)
VALUES (1, 1, now()),
       (2, 1, now()),
       (3, 1, now()),
       (4, 2, now());