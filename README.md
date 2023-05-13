[Проект TopJava-2](https://javaops.ru/view/topjava2)
===============================

#### Разбор решения [выпускного проекта TopJava](https://github.com/JavaOPs/topjava/blob/master/graduation.md)

- Исходный код взят из миграции TopJava на Spring Boot (без еды)
- На основе этого репозитория на курсе будет выполняться выпускной проект "Голосование за рестораны"

-------------------------------------------------------------

- Stack: [JDK 17](http://jdk.java.net/17/), Spring Boot 3.x, Lombok, H2, Caffeine Cache, SpringDoc OpenApi 2.x,
  Mapstruct, Liquibase
- Run: `mvn spring-boot:run` in root directory.

-----------------------------------------------------
[REST API documentation](http://localhost:8080/)  
Креденшелы:

```
User:  user@yandex.ru / password
Admin: admin@gmail.com / admin
Guest: guest@gmail.com / guest
```

Разграничение доступа к использованию бизнес логики происходит на основании
установленных ролей пользователей (ROLE_ADMIN или ROLE_USER)

1. Администраторы(ROLE_ADMIN) могу добавлять рестораны и ежедневное меню:

- Администратор может создать новый ресторан, отправив
  POST-запрос к endpoint /api/admin/restaurants
- Администратор может создать новое меню для ресторана, отправив
  POST-запрос к endpoint /api/restaurants/{restaurantId}/menus

2. Пользователи(ROLE_USER) могут голосовать за ресторан, где они хотят обедать сегодня:

- Пользователь может проголосовать за ресторан, отправив
  POST-запрос к endpoint /api/votes
- В голосовании указываются идентификаторы пользователя и ресторана
- Пользователь может голосовать только один раз в день(учитывается только последний голос)
- Если пользователь голосует снова в тот же день до 11:00 считается что он передумал и
  последний голос заменяет предыдущий голос.

3. Каждый ресторан предоставляет новое меню каждый день:

- Администратор может создать/обновить ежедневное меню для ресторана, отправив
  PUT-запрос к endpoint /api/restaurants/{restaurantId}/menus/{menuId}
- Администратор может удалить ежедневное меню для ресторана, отправив
  DELETE-запрос к endpoint /api/restaurants/{restaurantId}/menus/{menuId}

4. Если пользователь голосует снова в тот же день после 11:00, голос не может быть изменен:

- Приложение проверяет время, когда пользователь отправляет новый запрос на голосование.
- Если время после 11:00, приложение отклоняет новый запрос на голосование.

REST API endpoints and examples

1. Управление персональными данными (Доступ(GET\PUT\DELETE) - авторизованные пользователи; PUT - не авторизованные)
   GET /api/profile
   Пример - получение данных авторизованного пользователя user@yandex.ru / password
   curl -X 'GET' \
   'http://localhost:8080/api/profile' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic dXNlckB5YW5kZXgucnU6cGFzc3dvcmQ='

   PUT /api/profile
   Пример - изменение данных авторизованного пользователя user@yandex.ru / password
   curl -X 'PUT' \
   'http://localhost:8080/api/profile' \
   -H 'accept: */*' \
   -H 'Authorization: Basic dXNlckB5YW5kZXgucnU6cGFzc3dvcmQ=' \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Great user",
   "email": "user@yandex.ru",
   "password": "password"
   }'

   POST /api/profile
   Пример - создание нового пользователя (для неавторизованных)
   curl -X 'POST' \
   'http://localhost:8080/api/profile' \
   -H 'accept: application/json' \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Great user2",
   "email": "user2@yandex.ru",
   "password": "password"
   }'

   DELETE /api/profile
   Пример удаления собственного профиля пользователя  
   (на примере удаления созданного через PUT /api/profile ser2@yandex.ru / password)
   curl -X 'DELETE' \
   'http://localhost:8080/api/profile' \
   -H 'accept: */*' \
   -H 'Authorization: Basic dXNlcjJAeWFuZGV4LnJ1OnBhc3N3b3Jk'

2. Администрирование пользовательских аккаунтов (Доступ - ROLE_ADMIN)
   POST /api/admin/users - создание нового пользователя
   curl -X 'POST' \
   'http://localhost:8080/api/admin/users' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "testuser",
   "email": "testuser@gmail.com",
   "password": "password"
   }'

   GET /api/admin/users - получение всех пользователей
   curl -X 'GET' \
   'http://localhost:8080/api/admin/users' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

   GET /api/admin/users - получение пользователя по email
   curl -X 'GET' \
   'http://localhost:8080/api/admin/users/by-email?email=user%40yandex.ru' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

   GET /api/admin/users/{userId} - получение пользователя по ID
   curl -X 'GET' \
   'http://localhost:8080/api/admin/users/1' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

   PUT /api/admin/users/{userId} - обновление пользователя по ID
   curl -X 'PUT' \
   'http://localhost:8080/api/admin/users/1' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Userr",
   "email": "userr@yandex.ru",
   "password": "passwordd"
   }'

   PATCH /api/admin/users/{userId} - включение\отключение учетной записи по ID
   пример отключения пользователя c id 1
   curl -X 'PATCH' \
   'http://localhost:8080/api/admin/users/1?enabled=false' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

DELETE /api/admin/users/{userId} - удаление пользователя по ID
curl -X 'DELETE' \
'http://localhost:8080/api/admin/users/3' \
-H 'accept: */*' \
-H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

3. Администрирование ресторанов (Доступ - ROLE_ADMIN, AdminRestaurantController)
   POST /api/admin/restaurants - создание нового ресторана
   curl -X 'POST' \
   'http://localhost:8080/api/admin/restaurants' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Grill"
   }'

   GET /api/admin/restaurants - получение всех ресторанов
   curl -X 'GET' \
   'http://localhost:8080/api/admin/restaurants' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

   GET /api/admin/restaurants/{restaurantId} - получение ресторана по ID
   curl -X 'GET' \
   'http://localhost:8080/api/admin/restaurants/2' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

   PUT /api/admin/restaurants/{restaurantId} - обновление ресторана по ID
   curl -X 'PUT' \
   'http://localhost:8080/api/admin/restaurants/1' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Супер ресторан"
   }'

   DELETE /api/admin/restaurants/{restaurantId} - Удаление ресторана по ID
   curl -X 'DELETE' \
   'http://localhost:8080/api/admin/restaurants/3' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

4. Администрирование меню
   POST /api/admin/restaurants/{restaurantId}/menus - создание нового меню для ресторана на
   сегодняшнюю дату (с учетом бизнес логики)
   curl -X 'POST' \
   'http://localhost:8080/api/admin/restaurants/1/menus' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
   -H 'Content-Type: application/json' \
   -d '{
   "dish": "superfoodd",
   "price": 100
   }'

   GET /api/admin/restaurants/{restaurantId}/menus - получение всех меню конкретного ресторана
   curl -X 'GET' \
   'http://localhost:8080/api/admin/restaurants/1/menus' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

   PUT /api/admin/restaurants/{restaurantId}/menus/{menuId} - обновление конкретного меню(по id),
   в конкретном ресторане по его id Без валидации даты меню - админ понимает что делает)
   curl -X 'PUT' \
   'http://localhost:8080/api/admin/restaurants/1/menus/4' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
   -H 'Content-Type: application/json' \
   -d '{
   "dish": "Burger",
   "price": 100,
   "date": "2023-05-13"
   }'

DELETE /api/admin/restaurants/{restaurantId}/menus/{menuId} - для удаления меню в конкретном ресторане
curl -X 'DELETE' \
'http://localhost:8080/api/admin/restaurants/1/menus/4' \
-H 'accept: */*' \
-H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

5. Администрирование голосов (AdminVoteController)
   Авторизованные пользователи с ролью ROLE_ADMIN)
   GET /api/admin/votes - получение всех голосов
   curl -X 'GET' \
   'http://localhost:8080/api/admin/votes' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

   GET /api/admin/votes{id}
   информация по конкретному голосу
   curl -X 'GET' \
   'http://localhost:8080/api/admin/votes/2' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   
   DELETE /api/admin/votes{id}
   curl -X 'DELETE' \
   'http://localhost:8080/api/admin/votes/5' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

6. Vote Авторизованные пользователи с ролью ROLE_USER,
   GET /api/votes/dayMenu - получение списка доступных на сегодняшний день к голосованию ресторанов и их меню
   curl -X 'GET' \
   'http://localhost:8080/api/votes/dayMenu' \
   -H 'accept: */*' \
   -H 'Authorization: Basic dXNlckB5YW5kZXgucnU6cGFzc3dvcmQ='

   POST /api/votes - голосование за выбранный ресторан(по ID) с понравившемся меню
   curl -X 'POST' \
   'http://localhost:8080/api/votes' \
   -H 'accept: */*' \
   -H 'Authorization: Basic dXNlckB5YW5kZXgucnU6cGFzc3dvcmQ=' \
   -H 'Content-Type: application/json' \
   -d '{
   "restaurantId": 1
   }'












