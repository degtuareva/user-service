# user-service

Предоставляет возможность:

- GET Поиск пользователя по id
- GET Поиск пользователя по логину
- GET Проверка существования по id
- GET Проверка существования по логину
- GET Вывод всех пользователей с фильтром по адресным данным
- GET Вывод адресов привязанных к пользователю по его id
- POST Создание нового пользователя
- POST Привязка нового адреса к пользователю
- DELETE Удаление адреса
- DELETE Удаление пользователя

## Стек

- Hibernate (работа с entity, связи)
- JpaRepository
- SpringCore
- SpringMvc
- SpringValidation
- Swagger
- Profiles
- Liquibase (миграции)
- Mapstruct
- Глобальная обработка ошибок
- Postman
- PostgreSQL

## Версии

### version 2.0

Добавлена модель Address

#### version 2.1

Добавлена инкапсуляция для полей сущности Address

### version 3.0

Добавлена модель User

### version 4.0

Разработаны UserController, UserService, UserRepository. Создан следующий эндпоинт:

## UserController

### Get

#### Проверка наличия пользователя по id

#### Запрос:

```http request
http://localhost:9087/api/users/existence/id/{user_id}
```

#### boolean (true/false)

Bad request 400, если в качестве user_id переданы не валидные данные

### version 5.0

Создан следующий эндпоинт:

## UserController

### Get

#### Проверка наличия пользователя по логину

#### Запрос:

```http request
http://localhost:9087/api/users/existence/login/{login}
```

#### Ответ: boolean (true/false)

Bad request 400, если в качестве user_id переданы не валидные данные

### version 6.0

Создан следующий эндпойнт:

## UserController

### Get

#### Получение пользователя по логину. На входе логин пользователя, на выходе UserDto.

#### Запрос:

```http request
http://localhost:9087/api/users/login/user_1@gmail.com
```

#### Ответ:

```json
{
  "id": 4,
  "login": "user_1@gmail.com",
  "password": "$2a$10$/Z.pZ22wI6vxLD9/L8.D.OgKO57m2dsqRUOgtMDIloc3eQHB1IKLW",
  "role": "USER",
  "createDate": "2025-01-10T18:06:17.926168",
  "lastVisitDate": "2025-01-10T18:06:17.926202"
}
```

##### Возможные исключения: в случае, если пользователь не найден, выбрасываем исключение UserNotFoundException.

### version 7.0

Создан следующий эндпойнт:

## UserController

### Get

#### Получение пользователя по id. На входе id пользователя, на выходе UserDto.

#### Запрос:

```http request
http://localhost:9087/api/users/4
```

#### Ответ:

```json
{
  "id": 4,
  "login": "user_1@gmail.com",
  "password": "$2a$10$/Z.pZ22wI6vxLD9/L8.D.OgKO57m2dsqRUOgtMDIloc3eQHB1IKLW",
  "role": "USER",
  "createDate": "2025-01-10T18:06:17.926168",
  "lastVisitDate": "2025-01-10T18:06:17.926202"
}
```

##### Возможные исключения: в случае, если пользователь не найден, выбрасываем исключение UserNotFoundException.