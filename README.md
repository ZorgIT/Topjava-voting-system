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

TODO List (прикидка решения):

1. Сделан форк основного проекта - по заданию его нужно использовать в качестве основы
2. Описать модель данных.
   Определить схему БД
   Предварительно:
   Необходимо создать сущности описывающие рестораны (Restaurant), пользовательское (Vote) голосование, меню (Menu).
   Один ресторан - много меню(одно на один день; отношение one-to-many)
   Пользовательское голосование - много разных пользователей могут проголосовать за одно меню (отношение many-to-one)
3. Имплементировать слой репозитория для коммуникации с БД
   Spring Data JPA
   2 репозитория - Restaurant, Menu, Vote
4. Имплементировать сервисный слой
   Описываем CRUD через репозиторий
5. Имплементировать слой управления
   Restaurant, Menu, Vote.
   В контроллерах описывается обработка HTTP запросов и ответов, вызов сервисных методов и операций.
6. Определение конечных точек REST
   Описание ендпоинтов(за что и как дергать программу), тестирование через POSTMAN, curl,
   формирование финальной версии README с описанием функционала.
   Настройка, проверка, корректировка правил доступа на стандартных пользователях()

Подкорректировать возвращаемый результат по запросам (отправляемые объекты перевести на DTO)

todo:
REST API endpoints

1. Администрирование Меню (Доступ - ROLE_ADMIN, AdminMenuController) (checked):
   POST /api/admin/restaurants/{restaurantId}/menus - создание нового меню для ресторана на сегодняшнюю дату (checked)
   GET /api/admin/restaurants/{restaurantId}/menus - получение всех меню конкретного ресторана (checked)
   PUT /api/admin/restaurants/{restaurantId}/menus/{menuId} - обновление конкретного меню(по id), 
в конкретном ресторане по его id  Без валидации даты меню - админ понимает что делает) (checked)
   DELETE /api/admin/restaurants/{restaurantId}/menus/{menuId} - для удаления меню для в конкретном ресторане (checked)

2. Управление Персональными данными (Доступ(GET\PUT\DELETE) - авторизованные пользователи; POST - не авторизованные) (checked)
   POST /api/profile - для создания нового пользователя #todo Добавить валидацию данных (checked)
   GET /api/profile - получение данных текущего пользователя (checked)
   PUT /api/profile - обновление данных текущего пользователя (checked)
   DELETE /api/profile - удаление профиля пользователя (checked)

3. Администрирование пользовательских аккаунтов (Доступ - ROLE_ADMIN, AdminUserController) (checked)
   POST /api/admin/users - создание нового пользователя (checked)
   GET /api/admin/users - получение всех пользователей (checked)
   GET /api/admin/users - получение пользователя по email checked)
   GET /api/admin/users/{userId} - получение пользователя по ID (checked)
   PUT /api/admin/users/{userId} - обновление пользователя по ID (checked)
   PATCH /api/admin/users/{userId} - включение\отключение учетной записи по ID (checked)
   DELETE /api/admin/users/{userId} - удаление пользователя по ID (checked)

4. Администрирование ресторанов (Доступ - ROLE_ADMIN, AdminRestaurantController)  (checked)
   POST /api/admin/restaurants - создание нового ресторана (checked)
   GET /api/admin/restaurants - получение всех ресторанов (checked)
   GET /api/admin/restaurants/{restaurantId} - получение ресторана по ID (checked)
   PUT /api/admin/restaurants/{restaurantId} - обновление ресторана по ID  (checked)
   DELETE /api/admin/restaurants/{restaurantId} - Удаление ресторана по ID (checked)

5. VoteController (Доступ - авторизованные пользователи с ролью ROLE_USER)
   GET /api/votes - получение всех голосов
   POST /api/votes - для создания голоса

Описание бизнес логики с учетом использования REST api:

Разграничение доступа к использованию бизнес логики происходит на основании
установленных ролей пользователей (ROLE_ADMIN или ROLE_USER)

1. Администраторы(ROLE_ADMIN) могу добавлять рестораны и ежедневное меню:

- Администратор может создать новый ресторан, отправив
  POST-запрос к endpoint /api/admin/restaurants
- Администратор может !создать/назначить! новое меню для ресторана, отправив
  POST-запрос к endpoint /api/restaurants/{restaurantId}/menus

2. Пользователи(ROLE_USER) могут голосовать за ресторан, где они хотят обедать сегодня:

- Пользователь может проголосовать за ресторан, отправив
  POST-запрос к endpoint /api/votes
- В голосовании указываются идентификаторы пользователя и ресторана
- Пользователь может голосовать только один раз в день(учитывается только последний голос)
- Если пользователь голосует снова в тот же день до 11:00 считается что он передумал и
  последний голос заменяет предыдущий голос.

3. Каждый ресторан предоставляет новое меню каждый день:

- Администратор может обновить ежедневное меню для ресторана, отправив
  PUT-запрос к endpoint /api/restaurants/{restaurantId}/menus/{menuId}
- Администратор может удалить ежедневное меню для ресторана, отправив
  DELETE-запрос к endpoint /api/restaurants/{restaurantId}/menus/{menuId}

4. Если пользователь голосует снова в тот же день после 11:00, голос не может быть изменен:
- Приложение проверяет время, когда пользователь отправляет новый запрос на голосование.
- Если время после 11:00, приложение отклоняет новый запрос на голосование.









