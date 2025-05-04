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

### version 8.0

Создан следующий эндпойнт:

## UserController

### Delete

#### Удаление пользователя по id, на входе id пользователя, на выходе ничего нет.

#### Запрос:

```http request
http://localhost:9087/api/users/6
```

#### Ответ:

204 No Content

##### Исключений в данном эндпоинте не предусмотрено

### version 9.0

Создан следующий эндпойнт:

## UserController

### POST

#### Привязка адреса к пользователю. На входе AddressCreateDto, на выходе AddressDto.

#### Запрос:

```http request
http://localhost:9087/api/users/address
```

#### Тело запроса

```json
{
  "userId": 4,
  "apartment": 45,
  "housing": "",
  "house": 89,
  "street": "street_three",
  "city": "city_two",
  "postalCode": "kki980",
  "country": "country_two"
}
```

#### Ответ:

```json
{
  "id": 7,
  "userId": 4,
  "apartment": 45,
  "housing": "",
  "house": 89,
  "street": "street_three",
  "city": "city_two",
  "postalCode": "kki980",
  "country": "country_two"
}
```

##### Возможные исключения:

В приходящем dto по userId мы проверяем, существует ли пользователь с таким id, если он
не найден то выбрасываем исключение UserNotFoundException.

### version 11.0

Создан следующий эндпойнт:

## UserController

### GET

#### Получение пользователей с возможностью пагинации и фильтрации по адресным данным.

На входе: pageNumber, pageSize, country, postalCode, city, street, house, housing, apartment  
На выходе: PageContentDto<UserDto>.

#### Запрос:

```http request
http://localhost:9087/api/users
```

#### Ответ:

```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 5
  },
  "content": [
    {
      "id": 2,
      "login": "admin_1@gmail.com",
      "password": "$2a$10$07t5cgkYwoWv44L/HOmoMOstGKQm2kfFjT07a14NYcnQ987QqksRe",
      "role": "ADMIN",
      "createDate": "2025-01-10T18:05:49.444372",
      "lastVisitDate": "2025-01-10T18:05:49.444438"
    },
    {
      "id": 3,
      "login": "admin_2@gmail.com",
      "password": "$2a$10$UGKdzLjoPulUesY4o.rwm.c..yuQMIrQIk2L1MA.g9mzqy.Vg5Txq",
      "role": "ADMIN",
      "createDate": "2025-01-10T18:05:58.764599",
      "lastVisitDate": "2025-01-10T18:05:58.764639"
    },
    {
      "id": 4,
      "login": "user_1@gmail.com",
      "password": "$2a$10$/Z.pZ22wI6vxLD9/L8.D.OgKO57m2dsqRUOgtMDIloc3eQHB1IKLW",
      "role": "USER",
      "createDate": "2025-01-10T18:06:17.926168",
      "lastVisitDate": "2025-01-10T18:06:17.926202"
    },
    {
      "id": 5,
      "login": "user_2@gmail.com",
      "password": "$2a$10$9UAR98lahbyxK.w22PpJpufSBWiTQDjPQA2KXnfz6kgKOtJtdP3u2",
      "role": "USER",
      "createDate": "2025-01-10T18:06:25.192185",
      "lastVisitDate": "2025-01-10T18:06:25.192237"
    }
  ]
}
```

##### Исключений в данном эндпоинте не предусмотрено