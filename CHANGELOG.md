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

#### Запрос_1:

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
      "id": 1,
      "login": "Alex",
      "password": "1111",
      "role": "ADMIN",
      "createDate": "2025-05-05T18:00:08.533881",
      "lastVisitDate": "2025-05-05T18:00:08.533881"
    },
    {
      "id": 2,
      "login": "Maria",
      "password": "2222",
      "role": "ADMIN",
      "createDate": "2025-05-05T18:00:48.031381",
      "lastVisitDate": "2025-05-05T18:00:48.031381"
    },
    {
      "id": 3,
      "login": "Olya",
      "password": "3333",
      "role": "USER",
      "createDate": "2025-05-05T18:01:34.757179",
      "lastVisitDate": "2025-05-05T18:01:34.757179"
    },
    {
      "id": 4,
      "login": "Leonid",
      "password": "4444",
      "role": "USER",
      "createDate": "2025-05-05T18:01:54.52683",
      "lastVisitDate": "2025-05-05T18:01:54.52683"
    },
    {
      "id": 5,
      "login": "Tolya",
      "password": "5555",
      "role": "USER",
      "createDate": "2025-05-05T18:02:14.271097",
      "lastVisitDate": "2025-05-05T18:02:14.271097"
    }
  ]
}
```
#### Запрос_2:

```http request
http://localhost:9087/api/users?pageNumber=2
```
```json
{
  "page": {
    "pageNumber": 2,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 5
  },
  "content": []
}
```
#### Запрос_3:

```http request
http://localhost:9087/api/users?pageSize=2
```
```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 2,
    "totalPages": 3,
    "totalElements": 5
  },
  "content": [
    {
      "id": 1,
      "login": "Alex",
      "password": "1111",
      "role": "ADMIN",
      "createDate": "2025-05-05T18:00:08.533881",
      "lastVisitDate": "2025-05-05T18:00:08.533881"
    },
    {
      "id": 2,
      "login": "Maria",
      "password": "2222",
      "role": "ADMIN",
      "createDate": "2025-05-05T18:00:48.031381",
      "lastVisitDate": "2025-05-05T18:00:48.031381"
    }
  ]
}
```
#### Запрос_4:

```http request
http://localhost:9087/api/users?country=usa
```
```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 1
  },
  "content": [
    {
      "id": 4,
      "login": "Leonid",
      "password": "4444",
      "role": "USER",
      "createDate": "2025-05-05T18:01:54.52683",
      "lastVisitDate": "2025-05-05T18:01:54.52683"
    }
  ]
}
```
#### Запрос_5:

```http request
http://localhost:9087/api/users?postalCode=123456
```
```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 1
  },
  "content": [
    {
      "id": 2,
      "login": "Maria",
      "password": "2222",
      "role": "ADMIN",
      "createDate": "2025-05-05T18:00:48.031381",
      "lastVisitDate": "2025-05-05T18:00:48.031381"
    }
  ]
}
```
#### Запрос_6:

```http request
http://localhost:9087/api/users?city=chelyabinsk
```
```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 1
  },
  "content": [
    {
      "id": 5,
      "login": "Tolya",
      "password": "5555",
      "role": "USER",
      "createDate": "2025-05-05T18:02:14.271097",
      "lastVisitDate": "2025-05-05T18:02:14.271097"
    }
  ]
}
```
#### Запрос_7:

```http request
http://localhost:9087/api/users?street=kirkorova
```
```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 2
  },
  "content": [
    {
      "id": 3,
      "login": "Olya",
      "password": "3333",
      "role": "USER",
      "createDate": "2025-05-05T18:01:34.757179",
      "lastVisitDate": "2025-05-05T18:01:34.757179"
    },
    {
      "id": 5,
      "login": "Tolya",
      "password": "5555",
      "role": "USER",
      "createDate": "2025-05-05T18:02:14.271097",
      "lastVisitDate": "2025-05-05T18:02:14.271097"
    }
  ]
}
```
#### Запрос_8:

```http request
http://localhost:9087/api/users?house=17
```
```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 1
  },
  "content": [
    {
      "id": 4,
      "login": "Leonid",
      "password": "4444",
      "role": "USER",
      "createDate": "2025-05-05T18:01:54.52683",
      "lastVisitDate": "2025-05-05T18:01:54.52683"
    }
  ]
}
```
#### Запрос_9:

```http request
http://localhost:9087/api/users?housing=7
```
```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 1
  },
  "content": [
    {
      "id": 3,
      "login": "Olya",
      "password": "3333",
      "role": "USER",
      "createDate": "2025-05-05T18:01:34.757179",
      "lastVisitDate": "2025-05-05T18:01:34.757179"
    }
  ]
}
```
#### Запрос_10:

```http request
http://localhost:9087/api/users?apartment=4
```
```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 1
  },
  "content": [
    {
      "id": 1,
      "login": "Alex",
      "password": "1111",
      "role": "ADMIN",
      "createDate": "2025-05-05T18:00:08.533881",
      "lastVisitDate": "2025-05-05T18:00:08.533881"
    }
  ]
}
```
#### Запрос_11:

```http request
http://localhost:9087/api/users?street=kirkorova&house=24
```
```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 2
  },
  "content": [
    {
      "id": 3,
      "login": "Olya",
      "password": "3333",
      "role": "USER",
      "createDate": "2025-05-05T18:01:34.757179",
      "lastVisitDate": "2025-05-05T18:01:34.757179"
    },
    {
      "id": 5,
      "login": "Tolya",
      "password": "5555",
      "role": "USER",
      "createDate": "2025-05-05T18:02:14.271097",
      "lastVisitDate": "2025-05-05T18:02:14.271097"
    }
  ]
}
```
#### Запрос_12:

```http request
http://localhost:9087/api/users?city=minsk&country=belarus
```
```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 2
  },
  "content": [
    {
      "id": 1,
      "login": "Alex",
      "password": "1111",
      "role": "ADMIN",
      "createDate": "2025-05-05T18:00:08.533881",
      "lastVisitDate": "2025-05-05T18:00:08.533881"
    },
    {
      "id": 2,
      "login": "Maria",
      "password": "2222",
      "role": "ADMIN",
      "createDate": "2025-05-05T18:00:48.031381",
      "lastVisitDate": "2025-05-05T18:00:48.031381"
    }
  ]
}
```
#### Запрос_13:

```http request
http://localhost:9087/api/users?pageSize=2&pageNumber=3
```
```json
{
  "page": {
    "pageNumber": 3,
    "pageSize": 2,
    "totalPages": 3,
    "totalElements": 5
  },
  "content": [
    {
      "id": 5,
      "login": "Tolya",
      "password": "5555",
      "role": "USER",
      "createDate": "2025-05-05T18:02:14.271097",
      "lastVisitDate": "2025-05-05T18:02:14.271097"
    }
  ]
}
```
##### Исключений в данном эндпоинте не предусмотрено