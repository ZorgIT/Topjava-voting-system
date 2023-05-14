[![Codacy Badge](https://app.codacy.com/project/badge/Grade/d61659c7f5bf465eb147096531705863)](https://app.codacy.com/gh/ZorgIT/Topjava-voting-system/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[Project TopJava-28](https://javaops.ru/view/topjava2)
===============================

- The source code was taken from the TopJava migration to Spring Boot (without food)
- Based on this repository, the "Restaurant Voting" final project was developed.

-------------------------------------------------------------

- Stack: [JDK 17](http://jdk.java.net/17/), Spring Boot 3.x, Lombok, H2, Caffeine Cache, SpringDoc OpenApi 2.x,
  Mapstruct, Liquibase
- Run: `mvn spring-boot:run` in root directory.

-----------------------------------------------------
[REST API documentation](http://localhost:8080/)  
Credentials:

```
User:  user@yandex.ru / password
Admin: admin@gmail.com / admin
Guest: guest@gmail.com / guest
```

Access to the use of business logic is based on established user roles (ROLE_ADMIN or ROLE_USER).

### 1. Administrators (ROLE_ADMIN) can add restaurants and daily menus:

- An administrator can create a new restaurant by sending a
  POST request to the endpoint /api/admin/restaurants.
- An administrator can create a new menu for a restaurant by sending a
  POST request to the endpoint /api/restaurants/{restaurantId}/menus.

### 2. Users (ROLE_USER) can vote for the restaurant where they want to have 
lunch today:

- A user can vote for a restaurant by sending a
  POST request to the endpoint /api/votes.
- The vote includes the user and restaurant IDs.
- A user can only vote once a day (only the last vote is counted).
- If a user votes again on the same day before 11:00,
  it is considered that they changed their mind and the last vote replaces the previous one.

### 3. Each restaurant provides a new menu every day:

- An administrator can create/update the daily menu for a restaurant by sending a
  PUT request to the endpoint /api/restaurants/{restaurantId}/menus/{menuId}.
- An administrator can delete the daily menu for a restaurant by sending a
  DELETE request to the endpoint /api/restaurants/{restaurantId}/menus/{menuId}.

### 4. If a user votes again on the same day after 11:00, the vote cannot be 
changed:

- The application checks the time when the user submits a new vote request.
- If the time is after 11:00, the application rejects the new vote request.

-----------------------------------------------------
REST API endpoints and examples
-----------------------------------------------------

## 1. Personal data management 
(Access(GET\PUT\DELETE) - authorized users; PUT unauthorized users)
###   GET /api/profile
   Example - retrieve data of an authorized user user@yandex.ru / password
   ```
   curl -X 'GET' \
   'http://localhost:8080/api/profile' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic dXNlckB5YW5kZXgucnU6cGFzc3dvcmQ='
   ```

###   PUT /api/profile
   Example - modify data of an authorized user user@yandex.ru / password
   ```
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
   ```

###   POST /api/profile
   Example - create a new user (for unauthorized users)
   ```
   curl -X 'POST' \
   'http://localhost:8080/api/profile' \
   -H 'accept: application/json' \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Great user2",
   "email": "user2@yandex.ru",
   "password": "password"
   }'
   ```

###   DELETE /api/profile
   Example - delete user's own profile
   ```
   curl -X 'DELETE' \
   'http://localhost:8080/api/profile' \
   -H 'accept: */*' \
   -H 'Authorization: Basic dXNlckB5YW5kZXgucnU6cGFzc3dvcmQ'
   ```

## 2. User accounts administration
(Access - ROLE_ADMIN)
###   POST /api/admin/users
   create a new user
   ```
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
   ```

###   GET /api/admin/users
   retrieve all users
   ```
   curl -X 'GET' \
   'http://localhost:8080/api/admin/users' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

###   GET /api/admin/users
   retrieve a user by email
   ```
   curl -X 'GET' \
   'http://localhost:8080/api/admin/users/by-email?email=user%40yandex.ru' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

###   GET /api/admin/users/{userId}
   retrieve a user by ID
   ```
   curl -X 'GET' \
   'http://localhost:8080/api/admin/users/1' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

###   PUT /api/admin/users/{userId}
   update a user by ID
   ```
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
   ```

###   PATCH /api/admin/users/{userId}
   enable/disable a user account by ID 
   in example disable a user with ID 1
   ```
   curl -X 'PATCH' \
   'http://localhost:8080/api/admin/users/1?enabled=false' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

###   DELETE /api/admin/users/{userId}
   delete a user by ID
   ```
   curl -X 'DELETE' \
   'http://localhost:8080/api/admin/users/3' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

## 3. Restaurant administration 
(Access - ROLE_ADMIN)
###   POST /api/admin/restaurants
   create a new restaurant
   ```
   curl -X 'POST' \
   'http://localhost:8080/api/admin/restaurants' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Grill"
   }'
   ```

###   GET /api/admin/restaurants 
   retrieve all restaurants
   ```
   curl -X 'GET' \
   'http://localhost:8080/api/admin/restaurants' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

###   GET /api/admin/restaurants/{restaurantId}
   retrieve a restaurant by ID
   ```
   curl -X 'GET' \
   'http://localhost:8080/api/admin/restaurants/2' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

###   PUT /api/admin/restaurants/{restaurantId}
   update a restaurant by ID
   ```
   curl -X 'PUT' \
   'http://localhost:8080/api/admin/restaurants/1' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Super restaurant"
   }'
   ```

###   DELETE /api/admin/restaurants/{restaurantId}
   delete a restaurant by ID
   ```
   curl -X 'DELETE' \
   'http://localhost:8080/api/admin/restaurants/3' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

## 4. Menu administration
(Access - ROLE_ADMIN)
###   POST /api/admin/restaurants/{restaurantId}/menus 
   create a new menu for the restaurant on today's date (taking into account 
business logic)
   ```
   curl -X 'POST' \
   'http://localhost:8080/api/admin/restaurants/1/menus' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
   -H 'Content-Type: application/json' \
   -d '{
   "dish": "superfoodd",
   "price": 100
   }'
   ```

### GET /api/admin/restaurants/{restaurantId}/menus
   retrieve all menus for a specific restaurant
   ```
   curl -X 'GET' \
   'http://localhost:8080/api/admin/restaurants/1/menus' \
   -H 'accept: application/json' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

### PUT /api/admin/restaurants/{restaurantId}/menus/{menuId} 
   update a specific menu (by ID) in a specific restaurant (by ID) without menu 
date validation (admin understands what they are doing)
   ```
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
   ```

### DELETE /api/admin/restaurants/{restaurantId}/menus/{menuId}
   delete a menu in a specific restaurant
   ```
   curl -X 'DELETE' \
   'http://localhost:8080/api/admin/restaurants/1/menus/4' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

## 5. Vote administration
(Access - ROLE_ADMIN)
### GET /api/admin/votes
   retrieve all votes
   ```
   curl -X 'GET' \
   'http://localhost:8080/api/admin/votes' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

### GET /api/admin/votes{id} 
   retrieve information about a specific vote
   ```
   curl -X 'GET' \
   'http://localhost:8080/api/admin/votes/2' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

### DELETE /api/admin/votes{id} 
   delete a specific vote
   ```
   curl -X 'DELETE' \
   'http://localhost:8080/api/admin/votes/5' \
   -H 'accept: */*' \
   -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
   ```

## 6. Vote 
(Access - Authorized users with ROLE_USER)
### GET /api/votes/dayMenu
   retrieve a list of available restaurants and their menus for today's voting
   ```
   curl -X 'GET' \
   'http://localhost:8080/api/votes/dayMenu' \
   -H 'accept: */*' \
   -H 'Authorization: Basic dXNlckB5YW5kZXgucnU6cGFzc3dvcmQ='
   ```

### POST /api/votes
   vote for a selected restaurant (by ID) with a preferred 
menu
   ```
   curl -X 'POST' \
   'http://localhost:8080/api/votes' \
   -H 'accept: */*' \
   -H 'Authorization: Basic dXNlckB5YW5kZXgucnU6cGFzc3dvcmQ=' \
   -H 'Content-Type: application/json' \
   -d '{
   "restaurantId": 1
   }'
   ```
