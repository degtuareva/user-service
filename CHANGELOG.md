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

### version 10.0

Создан следующий эндпойнт:

## UserController

### GET

#### Разработан API по получению всех адресов,

#### привязанных к пользователю по его id. На входе id пользователя, на выходе List<AddressDto>

#### Запрос:

```http request
http://localhost:9087/api/users/address/2
```

#### Тело запроса

отсутствует


#### Ответ:
``` json
 {
        "id": 1,
        "userId": 2,
        "apartment": 0,
        "housing": "",
        "house": 5,
        "street": "osipova",
        "city": "grodno",
        "postalCode": "246758",
        "country": "Беларусь"
    },
    {
        "id": 2,
        "userId": 2,
        "apartment": 0,
        "housing": "",
        "house": 5,
        "street": "osipova",
        "city": "grodno",
        "postalCode": "246758",
        "country": "Беларусь"
    },
    {
        "id": 3,
        "userId": 2,
        "apartment": 0,
        "housing": "",
        "house": 5,
        "street": "osipova",
        "city": "grodno",
        "postalCode": "246758",
        "country": "Беларусь"
    },
    {
        "id": 4,
        "userId": 2,
        "apartment": 0,
        "housing": "",
        "house": 5,
        "street": "osipova",
        "city": "grodno",
        "postalCode": "246758",
        "country": "Беларусь"
    }
}
```

##### Возможные исключения:

В случае, если пользователь не существует, никаких исключений выбрасываться не должно.
Так же как и если возвращаемый список пустой.

### version 11.0

Создан следующий эндпоинт

## UserController

### Post

#### Создание нового пользователя. На входе UserInfoDto пользователя, на выходе UserDto.

#### Запрос

```http request
http://localhost:9082/api/users
```

#### Тело запроса

```json
{
  "login": "user_7@gmail.com",
  "password": "user_7@gmail.com",
  "role": "USER"
}
```

#### Ответ

```json
{
  "id": 12,
  "login": "user_7@gmail.com",
  "password": "user_7@gmail.com",
  "role": "USER",
  "createDate": "2025-01-26T21:43:35.854829",
  "lastVisitDate": "2025-01-26T21:43:35.854858"
}
```

#### Возможные исключения

Никаких исключений выбрасываться не должно